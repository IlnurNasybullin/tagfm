package io.github.ilnurnasybullin.tagfm.core.search;

/**
 * @author Ilnur Nasybullin
 */
public class UniquelyIdentifiableTagException extends IllegalStateException {
    public UniquelyIdentifiableTagException() {
    }

    public UniquelyIdentifiableTagException(String s) {
        super(s);
    }

    public UniquelyIdentifiableTagException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniquelyIdentifiableTagException(Throwable cause) {
        super(cause);
    }
}
