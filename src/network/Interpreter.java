package network;

import commands.Instruction;

import java.util.List;


public class Interpreter implements Runnable {
    private final SiloState siloState;
    private List<Instruction> instructions;
    private boolean isRunning = false;
    private Instruction currentInstruction;


    public Interpreter(SiloState siloState) {
        this.siloState = siloState;
    }

    public void run() {
        //Thread Behavior
        while (true) {
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

    //1 instruction from the silo must be executed then waits for all silos to reach this point
    public void step() {
        System.out.println("[STEPPING 1 INSTRUCTION]");
        currentInstruction = instructions.get(siloState.getInstructionIndex());
        currentInstruction.execute(siloState);

        siloState.setInstructionIndex(siloState.getInstructionIndex() + 1);
        if (siloState.getInstructionIndex() >= instructions.size()) {
                siloState.setInstructionIndex(0);
        }
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
}
