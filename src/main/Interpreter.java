package main;

import commands.Instruction;
import network.SiloState;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class Interpreter {
    private final SiloState siloState;
    private final List<Instruction> instructions;

    public Interpreter(SiloState siloState, List<Instruction> instructions) {
        this.siloState = siloState;
        this.instructions = instructions;
    }

    public void run() throws InterruptedException, BrokenBarrierException {
        while (true) { // Keep running the instructions in an infinite loop
            Instruction currentInstruction = instructions.get(siloState.getInstructionIndex());

            currentInstruction.execute(siloState);

            // Print the current state and instruction
            System.out.println(
                    "---\n" +
                            "Silo name: " + siloState.getName() + "\n" +
                            "commands.Instruction index: " + siloState.getInstructionIndex() + "\n"+
                            "Current instruction: " + currentInstruction.getClass().getSimpleName()+ "\n"+
                            "ACC: " + siloState.getAcc() + " | BAK: " + siloState.getBak() + "\n" +
                            "---");

            siloState.setInstructionIndex(siloState.getInstructionIndex() + 1);

            // Reset the instruction index to 0 if it reaches the end of the instructions list
            if (siloState.getInstructionIndex() >= instructions.size()) {
                siloState.setInstructionIndex(0);
            }

            try {
                // Sleep for 1 second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Restore the interrupted status
                Thread.currentThread().interrupt();
                break;
            }

            siloState.waitForSynchronization();

        }
    }

}
