package io.github.ilnurnasybullin.tagfm.api.service;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface NamespaceTagRemoverService<T> {
    void removeTag(T tag, TagRemovingStrategy strategy);
}
