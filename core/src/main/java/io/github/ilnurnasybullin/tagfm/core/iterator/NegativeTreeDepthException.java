package io.github.ilnurnasybullin.tagfm.core.iterator;

/**
 * @author Ilnur Nasybullin
 */
public class NegativeTreeDepthException extends IllegalArgumentException {
    public NegativeTreeDepthException() {
    }

    public NegativeTreeDepthException(String s) {
        super(s);
    }

    public NegativeTreeDepthException(String message, Throwable cause) {
        super(message, cause);
    }

    public NegativeTreeDepthException(Throwable cause) {
        super(cause);
    }
}
