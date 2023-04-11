package network;

import java.util.concurrent.SynchronousQueue;

public class Grid {
    private final int numRows;
    private final int numCols;
    private final SiloState[][] silos;
    private final SynchronousQueue<Integer>[][] queues;

    public Grid(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.silos = new SiloState[numRows][numCols];
        this.queues = new SynchronousQueue[numRows][numCols];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                this.queues[r][c] = new SynchronousQueue<>();
            }
        }
    }

    public SynchronousQueue<Integer> getQueue(int row, int col) {
        return queues[row][col];
    }

    public void setSilo(int row, int col, SiloState silo) {
        silos[row][col] = silo;
    }

    public SiloState getSilo(int row, int col) {
        //if out of bounds return null
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return null;
        }
        return silos[row][col];
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
}

