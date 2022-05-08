package io.github.ilnurnasybullin.tagfm.core.dto.tag;

public class InvalidTagNameException extends IllegalArgumentException {

    public InvalidTagNameException() {
    }

    public InvalidTagNameException(String s) {
        super(s);
    }

    public InvalidTagNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTagNameException(Throwable cause) {
        super(cause);
    }
}
