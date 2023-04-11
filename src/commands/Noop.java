package commands;
import network.SiloState;

public class Noop implements Instruction {

    public Noop(String[] strings) {
    }

    @Override
    public void execute(SiloState currentState) {
        currentState.noopMethod();
    }
}
