package commands;
import network.SiloState;

public class Negate implements Instruction {

    public Negate(String[] tokens) {
    }

    @Override
    public void execute(SiloState siloState) {
        int acc = siloState.getAcc();
        siloState.setAcc(-acc);
    }
}
