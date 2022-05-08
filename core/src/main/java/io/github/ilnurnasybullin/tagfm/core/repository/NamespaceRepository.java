package io.github.ilnurnasybullin.tagfm.core.repository;

import java.nio.file.Path;
import java.util.Optional;
import java.util.ServiceLoader;

public interface NamespaceRepository {
    Optional<Namespace> findBy(Path savedFile);
    void commit(Namespace namespace, Path savingFile);

    static NamespaceRepository get() {
        return ServiceLoader.load(NamespaceRepository.class)
                .findFirst()
                .orElseThrow();
    }
}
