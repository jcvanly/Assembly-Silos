package network;

import gui.SiloGraphic;
import commands.Instruction;
import javafx.application.Platform;
import java.util.List;
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
    private final Phaser phaser;
    private final SiloGraphic siloGraphic;

    private final Interpreter interpreter;
    private final Thread thread;

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

        siloGraphic = new SiloGraphic();

        interpreter = new Interpreter(this);
        thread = new Thread(interpreter);

        thread.start();
    }

    public SiloGraphic getSiloGraphic() {
        return siloGraphic;
    }

    /**
     * Waits for all silos to reach this point.
     */
    public void waitForSynchronization() {
        phaser.arriveAndAwaitAdvance();
    }

    /**
     * reads from a port
     * @param port The port to read from.
     * @return The value read from the port.
     */
    public int readFromPort(String port) {
        Platform.runLater(() -> siloGraphic.setModeVariable("READ"));
        phaser.arriveAndDeregister();
        Platform.runLater(() -> siloGraphic.setTransferLabelVisible(port, true));
        int value = network.receiveValue(row, col, port);
        System.out.println("Read from port: " + port + " value: " + value);
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
        Platform.runLater(() -> {
            System.out.println("Writing to port: " + port + " value: " + value);
            siloGraphic.setModeVariable("WRITE");
            siloGraphic.updateTransferValue(value, port);
            siloGraphic.setTransferLabelVisible(port,true); // Make the TransferLabel visible
        });
        phaser.arriveAndDeregister();

        network.sendValue(row, col, port, value);

        phaser.register();
        Platform.runLater(() -> {
            siloGraphic.setModeVariable("IDLE");
            siloGraphic.setTransferLabelVisible(port,false); // Hide the TransferLabel
        });
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
        Platform.runLater(() -> siloGraphic.setAccVariable(acc));
        this.acc = acc;
    }

    public int getBak() {
        Platform.runLater(() -> siloGraphic.setBakVariable(bak));
        return bak;
    }

    public void setBak(int bak) {
        Platform.runLater(() -> siloGraphic.setBakVariable(bak));
        this.bak = bak;
    }

    public int getInstructionIndex() {
        return instructionIndex;
    }

    public void setInstructionIndex(int instructionIndex) {
        this.instructionIndex = instructionIndex;
    }

    public void step() {
        interpreter.step();
    }

    public void pause() {
        interpreter.setRunning(false);
    }

    public void reset() {
        interpreter.setRunning(false);
        instructionIndex = 0;
        setAcc(0);
        setBak(0);

        siloGraphic.setTransferLabelVisible("UP", false);
        siloGraphic.setTransferLabelVisible("DOWN", false);
        siloGraphic.setTransferLabelVisible("LEFT", false);
        siloGraphic.setTransferLabelVisible("RIGHT", false);
    }

    public int getInstructionSize() {
        return interpreter.getInstructionSize();
    }

    public void startSilo() {
        String code = siloGraphic.getCodeArea();
        Parser parser = new Parser();
        List<Instruction> instructions = parser.parse(code);
        interpreter.setInstructions(instructions);
        interpreter.setRunning(true);
    }

    public void interruptThread() {
        thread.interrupt();
    }
}
