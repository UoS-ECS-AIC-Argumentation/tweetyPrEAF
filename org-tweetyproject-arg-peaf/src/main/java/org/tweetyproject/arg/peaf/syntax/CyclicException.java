package org.tweetyproject.arg.peaf.syntax;

public class CyclicException extends RuntimeException {

    public CyclicException() {
    }

    public CyclicException(String message) {
        super(message);
    }

    public CyclicException(String message, Throwable cause) {
        super(message, cause);
    }

    public CyclicException(Throwable cause) {
        super(cause);
    }

    protected CyclicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
