package commands;
import network.SiloState;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * Syntax: MOVE [SRC] [DST]
 * 
 * Read [SRC] and write the result to [DST]
 */
 
public class Move implements Instruction {
    private final String src;
    private final String dest;

    /**
     * Creates a new Move command.
     * @param src The source register or port.
     * @param dest The destination register or port.
     */
    public Move(String src, String dest) {
        this.src = src;
        this.dest = dest;
    }

    /**
     * Moves a value from one register or port to another.
     * @param siloState The current state of the silo.
     */
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
