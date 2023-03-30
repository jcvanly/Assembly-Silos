package commands;
import network.SiloState;

public class Move implements Instruction {
    private final String src;
    private final String dest;

    public Move(String src, String dest) {
        this.src = src;
        this.dest = dest;
    }

    @Override
    public void execute(SiloState siloState) {
        try {
            int value;
            if (siloState.isRegister(src)) {
                value = siloState.getRegisterValue(src);
            } else if (siloState.isPort(src)) {
                value = siloState.readFromPort(src);
            } else {
                value = Integer.parseInt(src);
            }

            if (siloState.isRegister(dest)) {
                siloState.setRegisterValue(dest, value);
            } else {
                siloState.writeToPort(dest, value);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric literal: " + e.getMessage());
        }
    }
}
