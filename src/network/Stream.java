package network;

import gui.StreamGraphic;
import javafx.application.Platform;

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
    private SynchronousQueue<Integer> queue;
    private boolean isRunning;
    private boolean isAlive = true;
    private Thread thread;

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
        isRunning = false;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        //While the input stream has values, output them to its SynchronousQueue
        while (isAlive) {
            if (isInput && isRunning) {
                if (currentIndex < values.size()) {
                    try {
                        queue.put(values.get(currentIndex));
                        currentIndex++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public void addValue(int value) {
        values.add(value);
        Platform.runLater(streamGraphic::updateGraphic);
    }

    public void kill() {
        isAlive = false;
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

    public SynchronousQueue<Integer> getQueue() {
        return queue;
    }
}
