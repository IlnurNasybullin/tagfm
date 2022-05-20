package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.Collection;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface NamespaceTagAdderService<T> {
    void addTag(T tag);
    default void addTags(Collection<T> tags) {
        tags.forEach(this::addTag);
    }
}
