package commands;

import network.SiloState;

public interface Instruction {
    void execute(SiloState currentState);
}
