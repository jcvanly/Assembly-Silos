package commands;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * The Instruction interface defines a contract for implementing various instructions
 * that can be executed by a silo. It makes use of the SiloState class, which is a part
 * of the 'network' package, to represent the current state of a silo during the execution
 * of instructions.
 */

import network.SiloState;

public interface Instruction {
    /**
     * The execute method is responsible for carrying out the specific action
     * associated with an instruction. It takes a SiloState object as a parameter,
     * allowing the instruction to modify the silo's state as needed.
     */
    void execute(SiloState currentState);
}
