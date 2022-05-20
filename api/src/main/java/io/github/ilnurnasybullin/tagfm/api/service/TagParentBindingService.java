package io.github.ilnurnasybullin.tagfm.api.service;

/**
 * @author Ilnur Nasybullin
 */
public interface TagParentBindingService<T> {
    void bindParent(T tag, T parent, TagParentBindingStrategy strategy);
    void unbind(T tag, TagParentBindingStrategy strategy);
}
