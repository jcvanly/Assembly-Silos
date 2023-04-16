package network;

import gui.StreamGraphic;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * The Stream class represents a stream in the SiloNetwork. The stream
 * can be an input stream (providing values to a silo) or an output stream
 * (receiving values from a silo). It provides methods for controlling the
 * execution of the stream, adding values to the input stream, and accessing
 * the attributes of the stream.  The class implements the Runnable interface
 * because each silo is running on a separate thread so each stream needs to
 * run on a separate thread as well.
 */

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

    /**
     * The constructor initializes the attributes such as row and col of the stream,
     * isInput, values, currentIndex, streamGraphic, queue, isRunning, isAlive, and
     * thread. The SynchronousQueue<Integer> object (queue) is used to facilitate
     * communication between the stream and the silo. The thread is started at
     * the end of the constructor.
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

    /**
     * The run method is the entry point for the thread. It is
     * an infinite loop that continues while the isAlive attribute
     * is true. If the stream is an input stream and is running, it
     * outputs the values in the values list to its SynchronousQueue.
     * Otherwise, the thread sleeps for 1000 milliseconds.
     */
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

    /**
     * The start and stop methods control the execution of the stream by
     * setting the isRunning attribute.
     */

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    /**
     * The addValue method adds a value to the values list and updates
     * the graphical representation of the stream.
     */
    public void addValue(int value) {
        values.add(value);
        Platform.runLater(streamGraphic::updateGraphic);
    }

    /**
     * The kill method sets the isAlive attribute to false, effectively stopping the thread.
     */
    public void kill() {
        isAlive = false;
    }

    /**
     * The getStreamGraphic, getValues, getRow, getCol, and getQueue methods are
     * getter methods for their corresponding attributes.
     */

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
