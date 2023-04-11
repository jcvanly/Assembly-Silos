package network;

import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.SynchronousQueue;

public class SiloNetwork {
    private final Grid grid;
    private final Phaser phaser;

    private final List<Stream> inputStreams;
    private final List<Stream> outputStreams;


    public SiloNetwork(int numRows, int numCols, List<Stream> inputStreams, List<Stream> outputStreams) {
        this.inputStreams = inputStreams;
        this.outputStreams = outputStreams;
        grid = new Grid(numRows, numCols);
        phaser = new Phaser(numRows * numCols );
    }

    public SiloState createSilo(int row, int col) {
        SiloState siloState = new SiloState(this, row, col);
        grid.setSilo(row, col, siloState);
        return siloState;
    }

    public synchronized void stopThreads() {
        for (int r = 0; r < grid.getNumRows(); r++) {
            for (int c = 0; c < grid.getNumCols(); c++) {
                SiloState siloState = grid.getSilo(r, c);
                siloState.getThread().interrupt();

            }
        }
    }

    public synchronized void startSilos() {
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


    public synchronized void stopSilos() {
        for (int r = 0; r < grid.getNumRows(); r++) {
            for (int c = 0; c < grid.getNumCols(); c++) {
                SiloState siloState = grid.getSilo(r, c);
                siloState.toggleExecution(false);
            }
        }
    }

    public synchronized void resetSilos() {
        for (int r = 0; r < grid.getNumRows(); r++) {
            for (int c = 0; c < grid.getNumCols(); c++) {
                SiloState siloState = grid.getSilo(r, c);
                siloState.reset();
            }
        }
    }

    public synchronized void stepSilos() {
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

    public Phaser getPhaser() {
        return phaser;
    }

    public int receiveValue(int r, int c, String port) throws InterruptedException {
        SynchronousQueue<Integer> queue;
        int value;
        switch (port) {
            case "UP" -> queue = grid.getQueue(r, c);
            case "DOWN" -> queue = grid.getQueue(r + 1, c);
            case "LEFT" -> queue = grid.getQueue(r, c - 1);
            case "RIGHT" -> queue = grid.getQueue(r, c + 1);
            default ->
                    throw new IllegalArgumentException("Invalid port: " + port);
        }
        value = queue.take();
        return value;
    }



    public void sendValue(int r, int c, String port, int value) throws InterruptedException {
        SynchronousQueue<Integer> queue = grid.getQueue(r , c);
        queue.put(value);
    }

    public List<Stream> getInputStreams() {
        return inputStreams;
    }

    public List<Stream> getOutputStreams() {
        return outputStreams;
    }
}

