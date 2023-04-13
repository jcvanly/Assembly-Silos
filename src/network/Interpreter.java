package network;

import commands.Instruction;

import java.util.List;


public class Interpreter implements Runnable {
    private final SiloState siloState;
    private List<Instruction> instructions;
    private boolean isRunning = false;
    private boolean isAlive = true;
    private boolean step = false;
    private Instruction currentInstruction;


    public Interpreter(SiloState siloState) {
        this.siloState = siloState;
    }

    public void run() {
        //Thread Behavior
        while (isAlive) {
            if (isRunning) {
                System.out.println("[RUNNING NEXT INSTRUCTION]");
                currentInstruction = instructions.get(siloState.getInstructionIndex());
                currentInstruction.execute(siloState);
                siloState.setInstructionIndex(siloState.getInstructionIndex() + 1);

                if (siloState.getInstructionIndex() >= instructions.size()) {
                    siloState.setInstructionIndex(0);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("[SILO INTERRUPTED]");
                }

                siloState.waitForSynchronization();
            } else if (step) {
                System.out.println("[STEPPING 1 INSTRUCTION]");
                currentInstruction = instructions.get(siloState.getInstructionIndex());
                currentInstruction.execute(siloState);
                siloState.setInstructionIndex(siloState.getInstructionIndex() + 1);

                if (siloState.getInstructionIndex() >= instructions.size()) {
                    siloState.setInstructionIndex(0);
                }

                siloState.waitForSynchronization();
                step = false;
            } else {
                System.out.println("[SILO WAITING]");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("[SILO INTERRUPTED]");
                }
            }
        }

    }
    public void setStep(boolean step) {
        this.step = step;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public int getInstructionSize() {
        return instructions.size();
    }

    public void kill() {
        isAlive = false;
    }
}
