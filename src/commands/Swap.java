package commands;
import network.SiloState;

public class Swap implements Instruction {
    public Swap(String[] strings) {
    }

    @Override
    public void execute(SiloState currentState) {
        int temp = currentState.getAcc();
        currentState.setAcc(currentState.getBak());
        currentState.setBak(temp);
    }
}
