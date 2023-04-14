package network;

import commands.Instruction;

import java.util.List;

/**
 * This class represents an interpreter for the SiloState objects.
 * It is responsible for executing instructions associated with a
 * specific SiloState. The class implements the Runnable interface,
 * because each silo is running on a different thread.
 */

public class Interpreter implements Runnable {
    private final SiloState siloState;
    private List<Instruction> instructions;
    private boolean isRunning = false;
    private boolean isAlive = true;
    private boolean step = false;
    private Instruction currentInstruction;


    /**
     * Initializes the siloState attribute with the provided SiloState object.
     */
    public Interpreter(SiloState siloState) {
        this.siloState = siloState;
    }

    /**
     * The run method is the entry point for the thread. It is an infinite loop
     * that continues while the isAlive attribute is true. The interpreter checks
     * the isRunning and step attributes to determine whether it should execute
     * instructions. If isRunning is true, the interpreter continuously executes
     * instructions and waits for synchronization with other silos. If step is
     * true, the interpreter executes a single instruction and then waits for
     * synchronization. If neither isRunning nor step is true, the thread
     * sleeps for 10 milliseconds.
     */
    public void run() {
        //Thread Behavior
        while (isAlive) {
            if (isRunning) {
                currentInstruction = instructions.get(siloState.getInstructionIndex());
                currentInstruction.execute(siloState);
                siloState.setInstructionIndex(siloState.getInstructionIndex() + 1);

                if (siloState.getInstructionIndex() >= instructions.size()) {
                    siloState.setInstructionIndex(0);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }

                siloState.waitForSynchronization();
            } else if (step) {
                currentInstruction = instructions.get(siloState.getInstructionIndex());
                currentInstruction.execute(siloState);
                siloState.setInstructionIndex(siloState.getInstructionIndex() + 1);

                if (siloState.getInstructionIndex() >= instructions.size()) {
                    siloState.setInstructionIndex(0);
                }

                siloState.waitForSynchronization();
                step = false;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
        }

    }

    /**
     * Sets the step attribute, which controls whether the interpreter should execute a single instruction.
     */
    public void setStep(boolean step) {
        this.step = step;
    }

    /**
     * Sets the isRunning attribute, which controls whether the interpreter
     * should continuously execute instructions or not.
     */
    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * Sets the instructions attribute, which is a list of Instruction
     * objects that the interpreter should execute.
     */
    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    /**
     * Returns the size of the instruction list
     */
    public int getInstructionSize() {
        return instructions.size();
    }

    /**
     * The kill method sets the isAlive attribute to false, effectively stopping the thread.
     * Extremely violent if you ask me.
     */
    public void kill() {
        isAlive = false;
    }
}
