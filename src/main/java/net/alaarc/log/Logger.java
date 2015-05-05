package net.alaarc.log;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

    private final Thread logPipeThread;
    private final Object logFinished = new Object();

    private final ILogMessageFormatter formatter;

    public Logger(PrintWriter writer, int queueCapacity, ILogMessageFormatter formatter) {
        this.writer = writer;
        this.messages = new ArrayBlockingQueue<>(queueCapacity);
        this.formatter = formatter;

        logPipeThread = new Thread(new LogPipe());
        logPipeThread.start();
    }

    public static class DefaultMessageFormatter implements ILogMessageFormatter {
        private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

        @Override
        public String format(LogMessage message) {
            String timestamp = DATE_FORMAT.format(message.getTimestamp());
            return "<" + message.getThreadName() + " @" + timestamp + "> " + message.getMessage();
        }
    }

    public static final ILogMessageFormatter DEFAULT_FORMATTER = new DefaultMessageFormatter();

    public Logger(PrintWriter writer) {
        this(writer, DEFAULT_CAPACITY, DEFAULT_FORMATTER);
    }

    public Logger(PrintWriter writer, ILogMessageFormatter formatter) {
        this(writer, DEFAULT_CAPACITY, formatter);
    }

    public Logger() {
        this(new PrintWriter(System.out), DEFAULT_CAPACITY, DEFAULT_FORMATTER);
    }

    public void log(LogMessage message) throws InterruptedException {
        messages.put(message);
    }

    private void appendMessage(LogMessage message) {
        writer.println(formatter.format(message));
        writer.flush();
    }

    public void finish() {
        logPipeThread.interrupt();
        synchronized (logFinished) {
            try {
                logFinished.wait();
            } catch (InterruptedException e) {
                // swallow it
            }
        }
    }

    private class LogPipe implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    LogMessage message = messages.take();
                    appendMessage(message);
                } catch (InterruptedException e) {
                    // https://youtrack.jetbrains.com/issue/IDEA-132897
                    //noinspection MismatchedQueryAndUpdateOfCollection
                    List<LogMessage> remainingMessages = new ArrayList<>();
                    messages.drainTo(remainingMessages);
                    remainingMessages.forEach(Logger.this::appendMessage);
                    writer.flush();
                    synchronized (logFinished) {
                        logFinished.notifyAll();
                    }
                    return;
                }
            }
        }
    }
}
