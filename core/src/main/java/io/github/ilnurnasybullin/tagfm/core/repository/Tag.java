package io.github.ilnurnasybullin.tagfm.core.repository;

import java.util.Optional;

public interface Tag {
    String name();
    <T extends Tag> Optional<T> parent();
}
