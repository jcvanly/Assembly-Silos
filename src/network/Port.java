package network;

import java.util.concurrent.Semaphore;

public class Port {
    private int value;
    private final Semaphore readSemaphore;
    private final Semaphore writeSemaphore;

    public Port() {
        value = 0;
        readSemaphore = new Semaphore(0);
        writeSemaphore = new Semaphore(1);
    }

    public int readValue() throws InterruptedException {
        readSemaphore.acquire();
        int currentValue = value;
        writeSemaphore.release();
        return currentValue;
    }

    public void writeValue(int value) throws InterruptedException {
        writeSemaphore.acquire();
        this.value = value;
        readSemaphore.release();
    }
}
