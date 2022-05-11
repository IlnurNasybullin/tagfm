package io.github.ilnurnasybullin.tagfm.core.dto.tag;

public class TreeTagCollisionException extends IllegalArgumentException {
    public TreeTagCollisionException() {
    }

    public TreeTagCollisionException(String s) {
        super(s);
    }

    public TreeTagCollisionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TreeTagCollisionException(Throwable cause) {
        super(cause);
    }
}
