package io.github.ilnurnasybullin.tagfm.cli.util;

/**
 * @author Ilnur Nasybullin
 */
public class TagNotAvailableForCreatingException extends IllegalStateException {
    public TagNotAvailableForCreatingException() {
    }

    public TagNotAvailableForCreatingException(String s) {
        super(s);
    }

    public TagNotAvailableForCreatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNotAvailableForCreatingException(Throwable cause) {
        super(cause);
    }
}
