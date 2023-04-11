package commands;
import network.SiloState;

public class Noop implements Instruction {

    public Noop() {
    }

    @Override
    public void execute(SiloState currentState) {
        currentState.noopMethod();
    }
}
