package network;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

/**
 * The Grid class in this Java code represents a grid of silos
 * in the SiloNetwork. It has a 2D array of SiloState objects
 * representing the silos and a 2D array of maps that contain
 * SynchronousQueues for communication between silos. Each map
 * in the queues array has four keys representing the four directions
 * (UP, DOWN, LEFT, RIGHT) and the corresponding SynchronousQueues.
 */

public class Grid {
    private final int numRows;
    private final int numCols;
    private final SiloState[][] silos;
    //Map of string to SynchronousQueue of Integer
    private final Map<String, SynchronousQueue<Integer>>[][] queues;

    /**
     * The constructor initializes the numRows, numCols, silos, and queues
     * attributes. It creates a HashMap for each cell in the grid, and for
     * each direction, it associates a new SynchronousQueue<Integer> with
     * the corresponding key.
     */
    public Grid(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.silos = new SiloState[numRows][numCols];
        this.queues = new Map[numRows][numCols];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                queues[r][c] = new HashMap<>();
                queues[r][c].put("UP", new SynchronousQueue<>());
                queues[r][c].put("DOWN", new SynchronousQueue<>());
                queues[r][c].put("LEFT", new SynchronousQueue<>());
                queues[r][c].put("RIGHT", new SynchronousQueue<>());
            }
        }
    }

    /**
     * The getQueue method returns the SynchronousQueue<Integer> associated
     * with a specific row, column, and direction.
     */
    public SynchronousQueue<Integer> getQueue(int row, int col, String direction) {
        return queues[row][col].get(direction);
    }

    /**
     * The setSilo method sets a SiloState object at a specific row and
     * column in the silos array.
     */
    public void setSilo(int row, int col, SiloState silo) {
        silos[row][col] = silo;
    }

    /**
     * The getSilo method returns the SiloState object at a specific row
     * and column in the silos array. If the row or column is out of
     * bounds, it returns null.
     */

    public SiloState getSilo(int row, int col) {
        //if out of bounds return null
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return null;
        }
        return silos[row][col];
    }

    /**
     * The getNumRows and getNumCols methods return the number of rows and
     * columns in the grid, respectively.
     */

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
}

