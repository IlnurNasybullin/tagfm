package io.github.ilnurnasybullin.tagfm.core.repository;

import java.util.Optional;
import java.util.ServiceLoader;

public interface NamespaceRepository {
    Optional<Namespace> findBy();
    void commit(Namespace namespace);

    static NamespaceRepository get() {
        return ServiceLoader.load(NamespaceRepository.class)
                .findFirst()
                .orElseThrow();
    }
}
