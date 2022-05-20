package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRepositoryService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.model.namespace.NamespaceMapper;
import io.github.ilnurnasybullin.tagfm.core.model.namespace.NamespaceRepoDto;
import io.github.ilnurnasybullin.tagfm.core.model.namespace.NamespaceSafety;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;

import java.util.Optional;

/**
 * @author Ilnur Nasybullin
 */
public class NamespaceRepositoryServiceImpl implements NamespaceRepositoryService<Namespace> {

    private final NamespaceRepository repository;

    private NamespaceRepositoryServiceImpl(NamespaceRepository repository) {
        this.repository = repository;
    }

    public static NamespaceRepositoryServiceImpl of(NamespaceRepository repository) {
        return new NamespaceRepositoryServiceImpl(repository);
    }

    public static NamespaceRepositoryServiceImpl of() {
        return of(NamespaceRepository.get());
    }

    @Override
    public Namespace init(String name, FileNamingStrategy strategy) {
        find(name).ifPresent(namespace -> {
            throw new NamespaceAlreadyInitializedException(String.format("Namespace [%s] already initialized!", namespace.name()));
        });

        return NamespaceSafety.init(name, strategy);
    }

    @Override
    public void commit(Namespace namespace) {
        repository.commit(NamespaceRepoDto.of(namespace));
    }

    @Override
    public Optional<Namespace> find(String name) {
        return repository.findBy(name)
                .map(namespace -> NamespaceMapper.of(namespace).mapping());
    }
}
