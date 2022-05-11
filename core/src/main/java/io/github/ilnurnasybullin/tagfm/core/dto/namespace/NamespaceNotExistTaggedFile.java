package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

public class NamespaceNotExistTaggedFile extends IllegalStateException {
    public NamespaceNotExistTaggedFile() {
    }

    public NamespaceNotExistTaggedFile(String s) {
        super(s);
    }

    public NamespaceNotExistTaggedFile(String message, Throwable cause) {
        super(message, cause);
    }

    public NamespaceNotExistTaggedFile(Throwable cause) {
        super(cause);
    }
}
