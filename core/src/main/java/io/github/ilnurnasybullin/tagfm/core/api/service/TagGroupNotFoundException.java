package io.github.ilnurnasybullin.tagfm.core.api.service;

public class TagGroupNotFoundException extends IllegalStateException {
    public TagGroupNotFoundException() {
    }

    public TagGroupNotFoundException(String s) {
        super(s);
    }

    public TagGroupNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagGroupNotFoundException(Throwable cause) {
        super(cause);
    }
}
