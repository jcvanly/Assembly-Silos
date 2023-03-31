package network;

import java.util.concurrent.CyclicBarrier;

public class SiloNetwork {
    private final Grid grid;
    private CyclicBarrier barrier;

    public SiloNetwork(int numRows, int numCols, int siloCount) {
        grid = new Grid(numRows, numCols);
        barrier = new CyclicBarrier(siloCount);
    }

    public SiloState createSilo(String name, int row, int col) {
        SiloState siloState = new SiloState(this, row, col, name);
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
                System.out.println(grid.getSilo(r,c).getName() + " getting ACC from " + neighborSilo.getName());
                return value;
            }
            case "DOWN" -> {
                neighborSilo = grid.getSilo(r + 1, c);
                value = neighborSilo.getAcc();
                System.out.println(grid.getSilo(r,c).getName() + " getting ACC from " + neighborSilo.getName());
                return value;
            }
            case "LEFT" -> {
                neighborSilo = grid.getSilo(r, c - 1);
                value = neighborSilo.getAcc();
                System.out.println(grid.getSilo(r,c).getName() + " getting ACC from " + neighborSilo.getName());
                return value;
            }
            case "RIGHT" -> {
                neighborSilo = grid.getSilo(r, c + 1);
                value = neighborSilo.getAcc();
                System.out.println(grid.getSilo(r,c).getName() + " getting ACC from " + neighborSilo.getName());
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
                System.out.println(grid.getSilo(r,c).getName() +" writing to " + neighborSilo.getName());
                neighborSilo.setAcc(value);
            }
            case "DOWN" -> {
                neighborSilo = grid.getSilo(r + 1, c);
                System.out.println(grid.getSilo(r,c).getName() +" writing to" + neighborSilo.getName());
                neighborSilo.setAcc(value);
            }
            case "LEFT" -> {
                neighborSilo = grid.getSilo(r, c - 1);
                System.out.println(grid.getSilo(r,c).getName() +" writing to" + neighborSilo.getName());
                neighborSilo.setAcc(value);
            }
            case "RIGHT" -> {
                neighborSilo = grid.getSilo(r, c + 1);
                System.out.println(grid.getSilo(r,c).getName() +" writing to" + neighborSilo.getName());
                neighborSilo.setAcc(value);
            }
            default -> {
                throw new IllegalArgumentException("Invalid port: " + port);
            }
        }
    }


    // 0 0
    // 0 0

}

