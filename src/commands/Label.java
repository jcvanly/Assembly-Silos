package commands;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * Syntax: :[LABEL]:
 *
 * Labels are used to mark a line of code to jump to. When jumped to
 * the instruction following the label is executed.
 */
 
import network.SiloState;

public class Label implements Instruction {
    private final int instructionIndex; // the index of the instruction within a silo, starts with 0

    public Label(int instructionIndex) {

        this.instructionIndex = instructionIndex;
    }

    public int getInstructionIndex() {
        return instructionIndex;
    }

    @Override
    public void execute(SiloState currentState) {
        // do nothing
    }
}
