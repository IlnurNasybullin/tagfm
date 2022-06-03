package io.github.ilnurnasybullin.tagfm.core.api.service;

public class IllegalTagForReplacing extends IllegalStateException {
    public IllegalTagForReplacing() {
    }

    public IllegalTagForReplacing(String s) {
        super(s);
    }

    public IllegalTagForReplacing(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTagForReplacing(Throwable cause) {
        super(cause);
    }
}
