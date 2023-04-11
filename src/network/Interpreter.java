package network;

import commands.Instruction;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

/**
 * The interpreter class that runs the instructions.
 */
public class Interpreter {
    private final SiloState siloState;
    private List<Instruction> instructions;
    private boolean isRunning = false;

    /**
     * Creates a new interpreter.
     * @param siloState The silo state.
     * @param instructions The list of instructions.
     */
    public Interpreter(SiloState siloState, List<Instruction> instructions) {
        this.siloState = siloState;
        this.instructions = instructions;
    }

    /**
     * Runs the instructions.
     * @throws InterruptedException If the thread is interrupted.
     * @throws BrokenBarrierException If the barrier is broken.
     */
    public void run() throws InterruptedException, BrokenBarrierException {
        while (true) {
            if (isRunning) {
                Instruction currentInstruction = instructions.get(siloState.getInstructionIndex());
                currentInstruction.execute(siloState);
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


                //Let the siloState know, its values have been transferred, so it can update the GUI
                siloState.setValuesChanged(true);

                SiloState.waitForSynchronization();
            } else {
                Thread.sleep(100);
            }
        }
    }

    public void step() throws InterruptedException {
        Instruction currentInstruction = instructions.get(siloState.getInstructionIndex());
        currentInstruction.execute(siloState);
        siloState.setInstructionIndex(siloState.getInstructionIndex() + 1);

        // Reset the instruction index to 0 if it reaches the end of the instructions list
        if (siloState.getInstructionIndex() >= instructions.size()) {
            siloState.setInstructionIndex(0);
        }
        SiloState.waitForSynchronization();

    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

}
