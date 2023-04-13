package commands;

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
