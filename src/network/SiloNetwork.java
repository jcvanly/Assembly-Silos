package network;

import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.SynchronousQueue;

/**
 * This code implements SiloNetwork which represents a network of silos and
 * input/output streams for the assembly silo simulation. The network consists
 * of a grid of SiloState objects, a list of input streams, a list of output
 * streams, and a Phaser to help synchronize the operation of silos. It provides
 * methods to start, pause, step, and stop the simulation.
 */
public class SiloNetwork {
    private final Grid grid;
    private final Phaser phaser;

    private final List<Stream> inputStreams;
    private final List<Stream> outputStreams;

    /**
     * The constructor initializes the grid, phaser, input and output streams.
     * @param numRows defines the row size of the grid
     * @param numCols defines the column size of the grid
     * @param inputStreams defines the input streams of the siloNetwork
     * @param outputStreams defines the output streams of the siloNetwork
     */
    public SiloNetwork(int numRows, int numCols, List<Stream> inputStreams, List<Stream> outputStreams) {
        this.inputStreams = inputStreams;
        this.outputStreams = outputStreams;
        grid = new Grid(numRows, numCols);
        phaser = new Phaser(numRows * numCols );
    }

    /**
     *  creates and returns a SiloState object, and sets it in the grid.
     * @param row the row of the silo in the grid
     * @param col the col of the silo in the grid
     */
    public SiloState createSilo(int row, int col) {
        SiloState siloState = new SiloState(this, row, col);
        grid.setSilo(row, col, siloState);
        return siloState;
    }

    /**
     * stopThreads, startSilos, startInputStreams, stopSilos, pauseSilos
     * and stepSilos are all used to control the flow of the program. These
     * functions are all triggered using the buttons created in the
     * AssemblySilosGUI class.
     */
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

    public void startInputStreams() {
        for (Stream inputStream : inputStreams) {
            inputStream.start();
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
                silo.stopThread();
            }
        }
    }

    public Phaser getPhaser() {
        return phaser;
    }

    /**
     * The receiveValue method handles receiving a value from a silo or stream.
     * @param r is the row of the silo
     * @param c is the col of the silo
     * @param port is where a silo would receive value from
     */
    public int receiveValue(int r, int c, String port) {
        int value = 0;
        SynchronousQueue<Integer> queue = grid.getQueue(r, c, port);
        for (Stream inputStream : inputStreams) {
            if (isSiloNextToStream(r, c, inputStream.getRow(), inputStream.getCol(), port)) {
                //sets queue to the queue of the stream
                queue = inputStream.getQueue();
            }
        }
        try {
            value = queue.take();
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting for value from silo");
        }
        return value;
    }

    /**
     * The sendValue method handles sending a value to a silo or stream.
     * @param r is the row of the silo
     * @param c is the col of the silo
     * @param port is where a silo would receive value from
     * @param value is the number being sent
     */
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

    /**
     * The isSiloNextToStream method checks if a silo is adjacent to a stream in the specified direction.
     * @param siloRow
     * @param siloCol
     * @param streamRow
     * @param streamCol
     * @param dir is the specified direction
     */
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

    /**
     * The stopStreams method stops all input and output streams.
     */
    public void stopStreams() {
        for (Stream inputStream : inputStreams) {
            inputStream.kill();
        }
        for (Stream outputStream : outputStreams) {
            outputStream.kill();
        }
    }
}