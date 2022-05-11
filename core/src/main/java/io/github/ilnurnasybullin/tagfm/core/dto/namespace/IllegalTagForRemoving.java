package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

public class IllegalTagForRemoving extends IllegalStateException {
    public IllegalTagForRemoving() {
    }

    public IllegalTagForRemoving(String s) {
        super(s);
    }

    public IllegalTagForRemoving(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTagForRemoving(Throwable cause) {
        super(cause);
    }
}
