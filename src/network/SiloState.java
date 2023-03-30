package network;

import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SiloState {
    private int acc;
    private int bak;
    private int instructionIndex;
    private int row;
    private int col;
    private CyclicBarrier barrier;
    private Map<String, Port> ports;
    private String name;

    public SiloState(Map<String, Port> ports, CyclicBarrier barrier, String name, int row, int col) {
        acc = 0;
        bak = 0;
        instructionIndex = 0;
        this.ports = ports;
        this.barrier = barrier;
        this.name = name;
        this.row = row;
        this.col = col;
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
        Port p = ports.get(port);
        if (p == null) {
            throw new IllegalArgumentException("Invalid port: " + port);
        }
        return p.readValue();
    }

    public void writeToPort(String port, int value) throws InterruptedException {
        ports.get(port).writeValue(value);
    }

    public boolean isRegister(String key) {
        return key.equalsIgnoreCase("ACC") || key.equalsIgnoreCase("BAK");
    }

    public boolean isPort(String key) {
        return ports.containsKey(key.toUpperCase());
    }

    public int getRegisterValue(String key) {
        if (key.equalsIgnoreCase("ACC")) {
            return acc;
        } else if (key.equalsIgnoreCase("BAK")) {
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
