package network;

import gui.StreamGraphic;
import java.util.ArrayList;
import java.util.List;

public class Stream {
    private final int row;
    private final int col;
    private final boolean isInput;
    private final List<Integer> values;
    private int currentIndex;
    private final StreamGraphic streamGraphic;

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
            System.out.println("Getting value " + value);
            currentIndex = (currentIndex + 1) % values.size();
            return value;
        } else {
            throw new UnsupportedOperationException("Cannot get value from output stream");
        }
    }
}
