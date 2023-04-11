package network;

import gui.SiloGraphic;
import commands.Instruction;
import javafx.application.Platform;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

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
    private static Phaser phaser;
    private final SiloGraphic siloGraphic;
    private Interpreter interpreter;
    private final Parser parser;
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
        phaser = network.getPhaser();
        this.network = network;

        parser = new Parser();
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

    /**
     * Waits for all silos to reach this point.
     */
    public static void waitForSynchronization() {
        phaser.arriveAndAwaitAdvance();
    }

    /**
     * reads from a port
     * @param port The port to read from.
     * @return The value read from the port.
     * @throws InterruptedException If the thread is interrupted.
     */
    public int readFromPort(String port) throws InterruptedException {
        Platform.runLater(() -> siloGraphic.setModeVariable("READ"));
        phaser.arriveAndDeregister();
        int value = network.receiveValue(row, col, port);
        phaser.register();
        return value;

    }

    /**
     * writes to a port
     * @param port The port to write to.
     * @param value The value to write.
     * @throws InterruptedException If the thread is interrupted.
     */
    public void writeToPort(String port, int value) throws InterruptedException {
        Platform.runLater(() -> siloGraphic.setModeVariable("WRITE"));
        Platform.runLater(() -> siloGraphic.updateTransferValue(value, port));
        phaser.arriveAndDeregister();
        Platform.runLater(() -> siloGraphic.setModeVariable("IDLE"));
        network.sendValue(row, col, port, value);
        siloGraphic.setTransferLabelVisible(port, false);
        phaser.register();
    }

    /**
     * Checks if the key is a register
     * @param key The key to check.
     * @return True if the key is a register, false otherwise.
     */
    public boolean isRegister(String key) {
        return key.equalsIgnoreCase("ACC") || key.equalsIgnoreCase("BAK");
    }

    /**
     * Checks if the key is a port
     * @param key The key to check.
     * @return True if the key is a port, false otherwise.
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
        Platform.runLater(() -> siloGraphic.setModeVariable("WRITE"));
        if (key.equalsIgnoreCase("ACC")) {
            setAcc(value);
        } else if (key.equalsIgnoreCase("BAK")) {
            setBak(value);
        } else {
            throw new IllegalArgumentException("Invalid register: " + key);
        }
    }

    public void noopMethod() {
        Platform.runLater(() -> siloGraphic.setModeVariable("IDLE"));
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
        if (thread != null && thread.isAlive()) {
            interpreter.setRunning(false);
        }
        siloGraphic.setCodeArea(currentCode);
        List<Instruction> instructions = parser.parse(currentCode);
        setInstructions(instructions);
    }

    public void setInstructions(List<Instruction> instructions) {
        if (thread != null && thread.isAlive()) {
            interpreter.setInstructions(instructions);
            interpreter.setRunning(true);
        } else {
            interpreter = new Interpreter(this, instructions);
            interpreter.setRunning(true);
            thread = new Thread(() -> {
                try {

                    interpreter.run();
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.println("Thread interrupted, shutting down");
                }
            });
        }
    }

    public void updateInstructionsFromGraphic() {
        String code = siloGraphic.getCodeArea();
        setCode(code);
    }

    public void toggleExecution(boolean running) {
        interpreter.setRunning(running);
        Platform.runLater(() -> siloGraphic.setModeVariable("IDLE"));
    }

    public void step() throws InterruptedException {
        interpreter.step();
    }

    public void reset() {
        interpreter.setRunning(false);
        instructionIndex = 0;
        setAcc(0);
        setBak(0);
    }

    public int getAccumulator() {
        return acc;
    }

    public int getInstructionSize() {
        return interpreter.getInstructionSize();
    }
}
