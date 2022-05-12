package io.github.ilnurnasybullin.tagfm.core.dto.tag;

public class NamespaceNotExistTagsException extends IllegalStateException {
    public NamespaceNotExistTagsException() {
        super();
    }

    public NamespaceNotExistTagsException(String s) {
        super(s);
    }

    public NamespaceNotExistTagsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NamespaceNotExistTagsException(Throwable cause) {
        super(cause);
    }
}
