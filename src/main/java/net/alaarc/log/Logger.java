package net.alaarc.log;

import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Simple queue-based logger. Should be ok to replace with {@link java.util.logging.Logger}.
 *
 * @author dnpetrov
 */
public class Logger {
    public static final int DEFAULT_CAPACITY = 64;

    private final BlockingQueue<LogMessage> messages;
    private final PrintWriter writer;

    public Logger(PrintWriter writer, int queueCapacity) {
        this.writer = writer;
        this.messages = new ArrayBlockingQueue<>(queueCapacity);
        start();
    }

    public Logger(PrintWriter writer) {
        this(writer, DEFAULT_CAPACITY);
    }

    public Logger(int queueCapacity) {
        this(new PrintWriter(System.out), queueCapacity);
    }

    public Logger() {
        this(new PrintWriter(System.out), DEFAULT_CAPACITY);
    }

    private void start() {
        new Thread(() -> {
            while (true) {
                try {
                    LogMessage message = messages.take();
                    if (message.isSpoiled()) {
                        flush();
                        break;
                    } else {
                        appendMessage(message);
                    }
                } catch (InterruptedException e) {
                    // Process interrupts, if any
                    break;
                }
            }
        }).start();
    }

    private void flush() {
        writer.flush();
    }

    public void log(LogMessage message) throws InterruptedException {
        messages.put(message);
    }

    private void appendMessage(LogMessage message) {
        writer.println("<" + message.getThreadName() + " @" + message.getTimestamp() + "> " + message.getMessage());
    }

    public void finish() {
        // sends a spoiled message
        try {
            log(LogMessage.spoiled());
        } catch (InterruptedException e) {
            // swallow it
        }
    }
}
