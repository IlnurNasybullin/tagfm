package io.github.ilnurnasybullin.tagfm.api.service;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface TagCreatorService<T> {
    T createByFullName(String fullName);
}
