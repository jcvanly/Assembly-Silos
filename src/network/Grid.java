package network;

public class Grid {
    private final int numRows;
    private final int numCols;
    private final SiloState[][] silos;

    public Grid(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        silos = new SiloState[numRows][numCols];
    }

    public void setSilo(int row, int col, SiloState silo) {
        silos[row][col] = silo;
    }

    public SiloState getSilo(int row, int col) {
        return silos[row][col];
    }
}

