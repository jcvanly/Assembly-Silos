package commands;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * Syntax: JLZ [LABEL]
 * 
 * Jumps control of the program to the instruction following the given
 * [LABEL] if the value in the register ACC is less than zero
 */
 
import network.SiloState;

public class Jlz implements Instruction {
    private final int jumpToInstructionIndex;

    public Jlz(String jumpToInstructionIndexStr) {
        this.jumpToInstructionIndex = Integer.parseInt(jumpToInstructionIndexStr);
    }

    @Override
    public void execute(SiloState currentState) {
        if (currentState.getAcc() < 0) {
            currentState.setInstructionIndex(jumpToInstructionIndex - 1); // Subtract 1 because the index will be incremented after this instruction is executed
        }
    }
}
