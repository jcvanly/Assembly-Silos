package network;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class SiloNetwork {
    private final Map<String, Port> sharedPorts;
    private final Grid grid;
    private CyclicBarrier barrier;

    public SiloNetwork(int numRows, int numCols, int siloCount) {
        sharedPorts = new HashMap<>();
        sharedPorts.put("UP", new Port());
        sharedPorts.put("RIGHT", new Port());
        sharedPorts.put("DOWN", new Port());
        sharedPorts.put("LEFT", new Port());
        grid = new Grid(numRows, numCols);
        barrier = new CyclicBarrier(siloCount);
    }

    public SiloState createSilo(String name, int row, int col) {
        SiloState siloState = new SiloState(sharedPorts, barrier, name, row, col);
        grid.setSilo(row, col, siloState);
        return siloState;
    }

    public Grid getGrid() {
        return grid;
    }
}

