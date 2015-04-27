package net.alaarc.log;

import java.util.Date;
import java.util.Objects;

/**
 * @author dnpetrov
 */
public class LogMessage {
    private final String threadName;
    private final Date timestamp;
    private final String message;

    private LogMessage(String threadName, Date timestamp, String message) {
        this.threadName = threadName;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getThreadName() {
        return threadName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public static LogMessage create(String threadName, String message) {
        Objects.requireNonNull(threadName);
        Objects.requireNonNull(message);
        return new LogMessage(threadName, new Date(System.currentTimeMillis()), message);
    }

    public static LogMessage create(String message) {
        Objects.requireNonNull(message);
        return new LogMessage(Thread.currentThread().getName(), new Date(System.currentTimeMillis()), message);
    }

    public static LogMessage spoiled() {
        return new LogMessage(null, null, null);
    }

    public boolean isSpoiled() {
        return threadName == null;
    }
}
