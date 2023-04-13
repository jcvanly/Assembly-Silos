package commands;

import network.SiloState;

public class Add implements Instruction {
    private final String src;

    public Add(String src) {
        this.src = src;
    }

    @Override
    public void execute(SiloState siloState) {
        int value;
        if (siloState.isRegister(src)) {
            value = siloState.getRegisterValue(src);
        } else if (siloState.isPort(src)) {
            value = siloState.readFromPort(src);
        } else {
            value = Integer.parseInt(src);
        }

        siloState.setAcc(siloState.getAcc() + value);
    }
}
