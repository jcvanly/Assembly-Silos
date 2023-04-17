package commands;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * Syntax: JNZ [LABEL]
 *
 * Jumps control of the program to the instruction following the given
 * [LABEL] if the value in the register ACC is not equal to zero
 */
 
import network.SiloState;

public class Jnz implements Instruction {
    private final int jumpToInstructionIndex;

    public Jnz(String jumpToInstructionIndexStr) {
        this.jumpToInstructionIndex = Integer.parseInt(jumpToInstructionIndexStr);
    }

    @Override
    public void execute(SiloState currentState) {
        if (currentState.getAcc() != 0) {
            currentState.setInstructionIndex(jumpToInstructionIndex - 1); // Subtract 1 because the index will be incremented after this instruction is executed
        }
    }
}
