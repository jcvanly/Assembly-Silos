package commands;
import network.SiloState;

public class Save implements Instruction {
    public Save(String[] strings) {
    }

    @Override
    public void execute(SiloState currentState) {
        currentState.setBak(currentState.getAcc());
    }
}
