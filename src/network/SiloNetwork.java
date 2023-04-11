package network;

import java.util.concurrent.CyclicBarrier;

public class SiloNetwork {
    private final Grid grid;
    private final CyclicBarrier barrier;

    public SiloNetwork(int numRows, int numCols, int siloCount) {
        grid = new Grid(numRows, numCols);
        barrier = new CyclicBarrier(siloCount);
    }

    public SiloState createSilo(int row, int col) {
        SiloState siloState = new SiloState(this, row, col);
        grid.setSilo(row, col, siloState);
        return siloState;
    }

    public void stopThreads() {
        for (int r = 0; r < grid.getNumRows(); r++) {
            for (int c = 0; c < grid.getNumCols(); c++) {
                SiloState siloState = grid.getSilo(r, c);
                siloState.getThread().interrupt();

            }
        }
    }

    public void startSilos() {
        for (int r = 0; r < grid.getNumRows(); r++) {
            for (int c = 0; c < grid.getNumCols(); c++) {
                SiloState siloState = grid.getSilo(r, c);
                if (siloState.getThread().isAlive()) {
                    siloState.updateInstructionsFromGraphic();
                    siloState.toggleExecution(true);
                } else {
                    siloState.startSilo();
                    siloState.updateInstructionsFromGraphic();
                    siloState.toggleExecution(true);
                }
            }
        }
    }


    public void stopSilos() {
        for (int r = 0; r < grid.getNumRows(); r++) {
            for (int c = 0; c < grid.getNumCols(); c++) {
                SiloState siloState = grid.getSilo(r, c);
                siloState.toggleExecution(false);
            }
        }
    }

    public void resetSilos() {
        for (int r = 0; r < grid.getNumRows(); r++) {
            for (int c = 0; c < grid.getNumCols(); c++) {
                SiloState siloState = grid.getSilo(r, c);
                siloState.reset();
            }
        }
    }


    public void stepSilos() {
        for (int r = 0; r < grid.getNumRows(); r++) {
            for (int c = 0; c < grid.getNumCols(); c++) {
                SiloState siloState = grid.getSilo(r, c);
                try {
                    siloState.step();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted Exception");
                }
            }
        }
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
        }
        return 0;
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
        }
    }
}

