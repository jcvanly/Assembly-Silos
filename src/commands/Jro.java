package commands;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * Syntax: JRO [SRC]
 *
 * Jumps control of the program to the instruction specified by the offset
 * which is the value contained within [SRC].
 *
 * If the offset is greater than the size of the program then you should
 * wrap around to the beginning of the program until you get to an offset
 * which is contained within the program. The same goes for negative
 * offsets
 */
 
import network.SiloState;

public class Jro implements Instruction {
    private final String src;

    public Jro(String src) {
        this.src = src;
    }

    @Override
    public void execute(SiloState currentState) {
        int offset;

        if (src.equals("ACC")) {
            offset = currentState.getAcc();
        } else {
            offset = Integer.parseInt(src);
        }

        int currentInstructionIndex = currentState.getInstructionIndex();
        int programSize = currentState.getInstructionSize();
        int targetInstructionIndex = (currentInstructionIndex + offset) % programSize;

        if (targetInstructionIndex < 0) {
            targetInstructionIndex += programSize;
        }

        currentState.setInstructionIndex(targetInstructionIndex - 1); // Subtract 1 because the index will be incremented after this instruction is executed
    }
}
