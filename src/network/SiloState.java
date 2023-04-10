package network;

import JavaFx.SiloGraphic;
import commands.Instruction;
import javafx.application.Platform;
import main.Interpreter;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Represents the state of a silo
 */
public class SiloState {
    private int acc;
    private int bak;
    private int instructionIndex;
    private final int row;
    private final int col;
    private final SiloNetwork network;
    private static CyclicBarrier barrier;
    private final SiloGraphic siloGraphic;
    private Interpreter interpreter;
    private List<Instruction> instructions;
    private Thread thread;

    /**
     * Creates a new SiloState.
     * @param network The network that this silo is a part of.
     * @param row The row of the silo.
     * @param col The column of the silo.
     */
    public SiloState(SiloNetwork network, int row, int col) {
        acc = 0;
        bak = 0;
        instructionIndex = 0;
        this.row = row;
        this.col = col;
        barrier = network.getBarrier();
        this.network = network;

        siloGraphic = new SiloGraphic();
    }

    public void startSilo() {
        thread.start();
    }

    public Thread getThread() {
        return thread;
    }

    public SiloGraphic getSiloGraphic() {
        return siloGraphic;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    /**
     * Waits for all silos to reach this point.
     */
    public static void waitForSynchronization() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads from a port
     * @param port
     * @return
     * @throws InterruptedException
     */
    public int readFromPort(String port) throws InterruptedException {
        return network.getValue(row, col, port);
    }

    /**
     * writes to a port
     * @param port
     * @param value
     * @throws InterruptedException
     */
    public void writeToPort(String port, int value) throws InterruptedException {
        network.setValue(row, col, port, value);
    }

    /**
     * Checks if the key is a register
     * @param key
     * @return
     */
    public boolean isRegister(String key) {
        return key.equalsIgnoreCase("ACC") || key.equalsIgnoreCase("BAK");
    }

    /**
     * Checks if the key is a port
     * @param key
     * @return
     */
    public boolean isPort(String key) {
        return key.equals("UP") || key.equals("DOWN") || key.equals("LEFT") || key.equals("RIGHT");
    }

    public int getRegisterValue(String key) {
        if (key.equals("ACC")) {
            return acc;
        } else if (key.equals("BAK")) {
            return bak;
        }
        throw new IllegalArgumentException("Invalid register: " + key);
    }

    public void setRegisterValue(String key, int value) {
        if (key.equalsIgnoreCase("ACC")) {
            acc = value;
        } else if (key.equalsIgnoreCase("BAK")) {
            bak = value;
        } else {
            throw new IllegalArgumentException("Invalid register: " + key);
        }
    }

    public int getAcc() {
        Platform.runLater(() -> siloGraphic.setAccVariable(acc));
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
        Platform.runLater(() -> siloGraphic.setAccVariable(acc));
    }

    public int getBak() {
        Platform.runLater(() -> siloGraphic.setBakVariable(bak));
        return bak;
    }

    public void setBak(int bak) {
        this.bak = bak;
        Platform.runLater(() -> siloGraphic.setBakVariable(bak));
    }

    public int getInstructionIndex() {
        return instructionIndex;
    }

    public void setInstructionIndex(int instructionIndex) {
        this.instructionIndex = instructionIndex;
    }

    public void setCode(String currentCode) {
        siloGraphic.setCodeArea(currentCode);
    }

    public void setInstructions(List<Instruction> instructions) {
        interpreter = new Interpreter(this, instructions);
        thread = new Thread(() -> {
            try {
                interpreter.run();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
    }

    public void toggleExecution() {
        interpreter.toggleExecution();
    }

    public void step() throws InterruptedException {
        interpreter.step();
    }
}
