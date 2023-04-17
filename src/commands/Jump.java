package commands;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * Syntax: JUMP [LABEL]
 *
 * Jumps control of the program to the instruction following the given
 * [LABEL]
 */

import network.SiloState;

public class Jump implements Instruction {
    private final int jumpToInstructionIndex;

    public Jump(String jumpToInstructionIndexStr) {
        this.jumpToInstructionIndex = Integer.parseInt(jumpToInstructionIndexStr);
    }

    @Override
    public void execute(SiloState currentState) {
        currentState.setInstructionIndex(jumpToInstructionIndex - 1); // Subtract 1 because the index will be incremented after this instruction is executed
    }
}

