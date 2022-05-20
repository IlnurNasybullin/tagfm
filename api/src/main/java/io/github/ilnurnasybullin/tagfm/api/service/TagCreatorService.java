package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.Optional;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface TagCreatorService<T> {
    Optional<T> deepCreate(String fullName);
}
