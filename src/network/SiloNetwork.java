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
                siloState.stopThread();
            }
        }
    }

    public synchronized void startSilos() {
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                SiloState silo = grid.getSilo(row, col);
                silo.startSilo();
            }
        }
    }

    public synchronized void pauseSilos() {
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                SiloState silo = grid.getSilo(row, col);
                silo.pause();
            }
        }
    }

    public void stepSilos() {
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                SiloState silo = grid.getSilo(row, col);
                if (silo != null) {
                    silo.step();
                }
            }
        }
    }

    public synchronized void stopSilos() {
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                SiloState silo = grid.getSilo(row, col);
                silo.pause();
                silo.reset();
                //Clear output stream
                for (Stream outputStream : outputStreams) {
                    outputStream.clear();
                }
                //reset input stream index to 0
                for (Stream inputStream : inputStreams) {
                    inputStream.resetIndex();
                }
            }
        }
    }

    public Phaser getPhaser() {
        return phaser;
    }

    public int receiveValue(int r, int c, String port) {
        for (Stream inputStream : inputStreams) {
            if (isSiloNextToStream(r, c, inputStream.getRow(), inputStream.getCol(), port)) {
                return inputStream.getNextValue();
            }
        }

        SynchronousQueue<Integer> queue;
        int value = 0;
        queue = grid.getQueue(r, c, port);
        try {
            value = queue.take();
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting for value");
        }
        return value;
    }

    public void sendValue(int r, int c, String port, int value) throws InterruptedException {
        for (Stream outputStream : outputStreams) {
            if (isSiloNextToStream(r, c, outputStream.getRow(), outputStream.getCol(), port)) {
                outputStream.addValue(value);
                return;
            }
        }
        SynchronousQueue<Integer> queue;
        switch (port) {
            case "UP" -> queue = grid.getQueue(r - 1, c, "DOWN");
            case "DOWN" -> queue = grid.getQueue(r + 1, c, "UP");
            case "LEFT" -> queue = grid.getQueue(r, c - 1, "RIGHT");
            case "RIGHT" -> queue = grid.getQueue(r, c + 1, "LEFT");
            default -> throw new IllegalArgumentException("Invalid port: " + port);
        }
        queue.put(value);
    }

    //checks if silo is adjacent to stream in the specified direction dir
    private boolean isSiloNextToStream(int siloRow, int siloCol, int streamRow, int streamCol, String dir) {
        switch (dir) {
            case "UP" -> {
                return siloRow - 1 == streamRow && siloCol == streamCol;
            }
            case "DOWN" -> {
                return siloRow + 1 == streamRow && siloCol == streamCol;
            }
            case "LEFT" -> {
                return siloCol - 1 == streamCol && siloRow == streamRow;
            }
            case "RIGHT" -> {
                return siloCol + 1 == streamCol && siloRow == streamRow;
            }
            default -> throw new IllegalArgumentException("Invalid direction: " + dir);
        }
    }


    public List<Stream> getInputStreams() {
        return inputStreams;
    }

    public List<Stream> getOutputStreams() {
        return outputStreams;
    }
}