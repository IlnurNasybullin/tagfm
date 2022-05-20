package io.github.ilnurnasybullin.tagfm.api.service;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface NamespaceRenamingService {
    void rename(String newName);
}
