package network;

import java.util.ArrayList;
import java.util.List;

public class Stream {
    private final int row;
    private final int col;
    private final boolean isInput;
    private final List<Integer> values;
    private int currentIndex;{

    }

    public Stream(int row, int col, boolean isInput) {
        this.row = row;
        this.col = col;
        this.isInput = isInput;
        this.values = new ArrayList<>();
        this.currentIndex = 0;
    }

    public void addValue(int value) {
        values.add(value);
    }

    public int getNextValue() {
        if (isInput) {
            int value = values.get(currentIndex);
            currentIndex = (currentIndex + 1) % values.size();
            return value;
        } else {
            throw new UnsupportedOperationException("Cannot get value from output stream");
        }
    }

    public void writeValue() {

    }
}
