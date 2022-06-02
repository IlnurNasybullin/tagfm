package io.github.ilnurnasybullin.tagfm.api.service;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface NamespaceTagRemovingService<T> {
    void removeTag(T tag, TagRemovingStrategy strategy);
}
