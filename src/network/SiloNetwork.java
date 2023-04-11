package network;

import java.util.concurrent.CyclicBarrier;

public class SiloNetwork {
    private final Grid grid;
    private CyclicBarrier barrier;

    public SiloNetwork(int numRows, int numCols, int siloCount) {
        grid = new Grid(numRows, numCols);
        barrier = new CyclicBarrier(siloCount);
    }

    public SiloState createSilo(int row, int col) {
        SiloState siloState = new SiloState(this, row, col);
        grid.setSilo(row, col, siloState);
        return siloState;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public int getValue(int r, int c, String port) {
        SiloState neighborSilo;
        int value;
        switch (port) {
            case "UP" -> {
                neighborSilo = grid.getSilo(r - 1, c);
                value = neighborSilo.getAcc();
                return value;
            }
            case "DOWN" -> {
                neighborSilo = grid.getSilo(r + 1, c);
                value = neighborSilo.getAcc();
                return value;
            }
            case "LEFT" -> {
                neighborSilo = grid.getSilo(r, c - 1);
                value = neighborSilo.getAcc();
                return value;
            }
            case "RIGHT" -> {
                neighborSilo = grid.getSilo(r, c + 1);
                value = neighborSilo.getAcc();
                return value;
            }
            default -> {
                throw new IllegalArgumentException("Invalid port: " + port);
            }
        }
    }

    public void setValue(int r, int c, String port, int value) {
        SiloState neighborSilo;
        switch (port) {
            case "UP" -> {
                neighborSilo = grid.getSilo(r - 1, c);
                neighborSilo.setAcc(value);
            }
            case "DOWN" -> {
                neighborSilo = grid.getSilo(r + 1, c);
                neighborSilo.setAcc(value);
            }
            case "LEFT" -> {
                neighborSilo = grid.getSilo(r, c - 1);
                neighborSilo.setAcc(value);
            }
            case "RIGHT" -> {
                neighborSilo = grid.getSilo(r, c + 1);
                neighborSilo.setAcc(value);
            }
            default -> {
                System.out.println("Invalid port: " + port);
            }
        }
    }

    public SiloState getSiloState(int row, int col) {
        return grid.getSilo(row, col);
    }
}

