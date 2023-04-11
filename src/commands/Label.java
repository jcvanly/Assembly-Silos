package commands;

import network.SiloState;

public class Label implements Instruction {
    private final String name;
    private final int instructionIndex;

    public Label(String name, int instructionIndex) {
        this.name = name;
        this.instructionIndex = instructionIndex;
    }

    public String getName() {
        return name;
    }

    public int getInstructionIndex() {
        return instructionIndex;
    }

    @Override
    public void execute(SiloState currentState) throws InterruptedException {
        // do nothing
    }
}