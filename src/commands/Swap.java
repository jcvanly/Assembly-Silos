package commands;
import network.SiloState;

/**
 * Swaps the values of the ACC and BAK registers.
 */
public class Swap implements Instruction {
    /**
     * Creates a new Swap command.
     * @param strings The command arguments.
     */
    public Swap(String[] strings) {
    }

    /**
     * Swaps the values of the ACC and BAK registers.
     * @param currentState The current state of the silo.
     */
    @Override
    public void execute(SiloState currentState) {
        int temp = currentState.getAcc();
        currentState.setAcc(currentState.getBak());
        currentState.setBak(temp);
    }
}
