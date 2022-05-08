package io.github.ilnurnasybullin.tagfm.api.service;

public interface NamespaceService<N> {
    N init(String name);
    N init(String name, FileNamingStrategy strategy);
}
