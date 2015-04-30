package net.alaarc.log;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple queue-based logger. Should be ok to replace with {@link java.util.logging.Logger}.
 *
 * @author dnpetrov
 */
public class Logger {
    public static final int DEFAULT_CAPACITY = 64;

    private final BlockingQueue<LogMessage> messages;
    private final PrintWriter writer;

    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

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
        new Thread(new LogListener()).start();
    }

    private void flush() {
        writer.flush();
    }

    public void log(LogMessage message) throws InterruptedException {
        messages.put(message);
    }

    private void appendMessage(LogMessage message) {
        String timestamp = dateFormat.format(message.getTimestamp());
        writer.println("<" + message.getThreadName() + " @" + timestamp + "> " + message.getMessage());
        writer.flush();
    }


    private final Lock logFinishedLock = new ReentrantLock();
    private final Condition logFinished = logFinishedLock.newCondition();


    public void finish() {
        // sends a spoiled message
        try {
            logFinishedLock.lock();
            log(LogMessage.spoiled());
            logFinished.await();
        } catch (InterruptedException e) {
            // swallow it
        } finally {
            logFinishedLock.unlock();
        }
    }

    private class LogListener implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    LogMessage message = messages.take();
                    if (message.isSpoiled()) {
                        logFinishedLock.lock();
                        logFinished.signalAll();
                        logFinishedLock.unlock();
                        break;
                    } else {
                        Logger.this.appendMessage(message);
                    }
                } catch (InterruptedException e) {
                    // Process interrupts, if any
                    break;
                }
            }
        }
    }
}
