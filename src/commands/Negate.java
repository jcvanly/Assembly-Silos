package commands;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * Syntax: NEGATE
 *
 * The value of the register ACC is negated, zero remains zero
 */

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
