package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.api.service.NamespaceService;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;

import java.util.Optional;

public class NamespaceServiceImpl implements NamespaceService<NamespaceDto> {

    private final NamespaceRepository repository;

    public NamespaceServiceImpl(NamespaceRepository repository) {
        this.repository = repository;
    }

    public NamespaceServiceImpl() {
        this(NamespaceRepository.get());
    }

    public void commit(NamespaceDto namespace) {
        repository.commit(namespace);
    }

    public Optional<NamespaceDto> find() {
        return repository.findBy()
                .map(Namespace::from);
    }

    @Override
    public NamespaceDto init(String name) {
        return init(name, FileNamingStrategy.RELATIVE);
    }

    @Override
    public NamespaceDto init(String name, FileNamingStrategy strategy) {
        find().ifPresent(namespace -> {
            throw new NamespaceAlreadyInitialized(String.format("Namespace [%s] already initialized!", namespace.name()));
        });

        return Namespace.init(name, strategy);
    }
}
