package network;

import gui.StreamGraphic;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class Stream implements Runnable {
    private final int row;
    private final int col;
    private final boolean isInput;
    private final List<Integer> values;
    private int currentIndex;
    private final StreamGraphic streamGraphic;
    private final SynchronousQueue<Integer> queue;

    /***
     * Creates a new stream
     * @param row The row of the stream
     * @param col The column of the stream
     * @param isInput True if the stream is an input stream, false if it is an output stream
     */
    public Stream(int row, int col, boolean isInput) {
        this.row = row;
        this.col = col;
        this.isInput = isInput;
        this.values = new ArrayList<>();
        this.currentIndex = 0;
        streamGraphic = new StreamGraphic(this);
        queue = new SynchronousQueue<>();
        new Thread(this).start();
    }

    public void addValue(int value) {
        values.add(value);
        streamGraphic.updateGraphic();
    }

    public StreamGraphic getStreamGraphic() {
        return streamGraphic;
    }

    public List<Integer> getValues() {
        return values;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getNextValue() {
        if (isInput) {
            int value = values.get(currentIndex);
            currentIndex += 1;
            return value;
        }
        return -1;
    }

    public void clear() {
        if (!isInput) {
            values.clear();
            streamGraphic.updateGraphic();
        }
    }

    public void resetIndex() {
        if (isInput) {
            currentIndex = 0;
        }
    }

    public boolean hasNextValue() {
        return currentIndex < values.size();
    }

    @Override
    public void run() {
        //While the input stream has values, output them to its SynchronousQueue
        while (hasNextValue()) {
            int value = getNextValue();
            try {
                queue.put(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public SynchronousQueue<Integer> getQueue() {
        return queue;
    }
}
