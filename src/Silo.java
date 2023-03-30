import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

class Silo implements Runnable {
    private List<Instruction> program;
    private Map<String, BlockingQueue<Integer>> ports;
    private int acc;
    private int bak;
    private int instructionIndex;
    private CyclicBarrier barrier;

    public Silo(List<Instruction> program, CyclicBarrier barrier) {
        this.program = program;
        this.ports = new HashMap<>();
        this.acc = 0;
        this.bak = 0;
        this.instructionIndex = 0;
        this.barrier = barrier;

        for (String direction : new String[]{"UP", "RIGHT", "DOWN", "LEFT"}) {
            ports.put(direction, new LinkedBlockingQueue<>());
        }
    }

    public void setPort(String direction, BlockingQueue<Integer> port) {
        ports.put(direction, port);
    }

    public BlockingQueue<Integer> getPort(String direction) {
        return ports.get(direction);
    }

    private int getSrcValue(String src) throws InterruptedException {
        if (ports.containsKey(src)) {
            return ports.get(src).take();
        } else if (src.equals("ACC")) {
            return acc;
        } else if (src.equals("BAK")) {
            return bak;
        } else if (src.equals("NIL")) {
            return 0;
        } else {
            return Integer.parseInt(src);
        }
    }

    private void setDstValue(String dst, int value) throws InterruptedException {
        if (ports.containsKey(dst)) {
            ports.get(dst).put(value);
        } else if (dst.equals("ACC")) {
            acc = value;
        } else if (dst.equals("NIL")) {
            // Do nothing, as writing to NIL has no effect
        } else {
            throw new IllegalArgumentException("Invalid destination: " + dst);
        }
    }

    private int findLabel(String label) {
        for (int i = 0; i < program.size(); i++) {
            Instruction instruction = program.get(i);
            if (instruction.name.equals(":" + label + ":")) {
                return i;
            }
        }
        throw new IllegalArgumentException("Label not found: " + label);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Instruction instruction = program.get(instructionIndex);
                String name = instruction.name;
                List<String> args = instruction.args;

                switch (name) {
                    case "NOOP":
                        break;
                    case "MOVE":
                        setDstValue(args.get(1), getSrcValue(args.get(0)));
                        break;
                    case "SWAP":
                        int temp = acc;
                        acc = bak;
                        bak = temp;
                        break;
                    case "SAVE":
                        bak = acc;
                        break;
                    case "ADD":
                        acc += getSrcValue(args.get(0));
                        break;
                    case "SUB":
                        acc -= getSrcValue(args.get(0));
                        break;
                    case "NEGATE":
                        acc = -acc;
                        break;
                    case "JUMP":
                        instructionIndex = findLabel(args.get(0));
                        break;
                    case "JEZ":
                        if (acc == 0) {
                            instructionIndex = findLabel(args.get(0));
                        }
                        break;
                    case "JNZ":
                        if (acc != 0) {
                            instructionIndex = findLabel(args.get(0));
                        }
                        break;
                    case "JGZ":
                        if (acc > 0) {
                            instructionIndex = findLabel(args.get(0));
                        }
                        break;
                    case "JLZ":
                        if (acc < 0) {
                            instructionIndex = findLabel(args.get(0));
                        }
                        break;
                    case "JRO":
                        int offset = getSrcValue(args.get(0));
                        instructionIndex = (instructionIndex + offset) % program.size();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid instruction: " + name);
                }
                instructionIndex = (instructionIndex + 1) % program.size(); // Move to the next instruction
                barrier.await();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}