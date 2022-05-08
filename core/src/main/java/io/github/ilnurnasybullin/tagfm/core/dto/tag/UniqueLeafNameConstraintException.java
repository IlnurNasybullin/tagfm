package io.github.ilnurnasybullin.tagfm.core.dto.tag;

public class UniqueLeafNameConstraintException extends IllegalArgumentException {
    public UniqueLeafNameConstraintException() {
    }

    public UniqueLeafNameConstraintException(String s) {
        super(s);
    }

    public UniqueLeafNameConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueLeafNameConstraintException(Throwable cause) {
        super(cause);
    }
}
