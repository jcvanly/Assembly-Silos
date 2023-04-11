package network;

import gui.StreamGraphic;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class Stream {
    private final int row;
    private final int col;
    private final boolean isInput;
    private final List<Integer> values;
    private int currentIndex;
    private StreamGraphic streamGraphic;

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
    }

    /***
     * Adds a value to the stream
     * @param value
     */
    public void addValue(int value) {
        values.add(value);
        streamGraphic.updateGraphic();
    }

    /***
     * Gets next value from the stream
     * @return The next value in the stream
     */
    public int getNextValue() {
        if (isInput) {
            int value = values.get(currentIndex);
            currentIndex = (currentIndex + 1) % values.size();
            return value;
        } else {
            throw new UnsupportedOperationException("Cannot get value from output stream");
        }
    }

    /***
     * Writes a value to the stream
     * @param value The value to write to the stream
     */
    public void writeValue() {

    }

    public StreamGraphic getStreamGraphic() {
        return streamGraphic;
    }

    public boolean isInput() {
        return isInput;
    }

    //gets list of values
    public List<Integer> getValues() {
        return values;
    }

}
