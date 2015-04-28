package net.alaarc.vm;

/**
 * @author dnpetrov
 */
public class VmException extends RuntimeException {
    public VmException() {
    }

    public VmException(String message) {
        super(message);
    }

    public VmException(String message, Throwable cause) {
        super(message, cause);
    }

    public VmException(Throwable cause) {
        super(cause);
    }

    public VmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
