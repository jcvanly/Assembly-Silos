package network;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SiloState {
    private int acc;
    private int bak;
    private int instructionIndex;
    private int row;
    private int col;
    private SiloNetwork network;
    private CyclicBarrier barrier;
    private String name;

    public SiloState(SiloNetwork network, int row, int col, String name) {
        acc = 0;
        bak = 0;
        instructionIndex = 0;
        this.row = row;
        this.col = col;
        this.barrier = network.getBarrier();
        this.name = name;
        this.network = network;
    }

    public String getName() {
        return name;
    }

    public void waitForSynchronization() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public int readFromPort(String port) throws InterruptedException {
        return network.getValue(row, col, port);
    }

    public void writeToPort(String port, int value) throws InterruptedException {
        network.setValue(row, col, port, value);
    }

    public boolean isRegister(String key) {
        return key.equalsIgnoreCase("ACC") || key.equalsIgnoreCase("BAK");
    }

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
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public int getBak() {
        return bak;
    }

    public void setBak(int bak) {
        this.bak = bak;
    }

    public int getInstructionIndex() {
        return instructionIndex;
    }

    public void setInstructionIndex(int instructionIndex) {
        this.instructionIndex = instructionIndex;
    }

}
