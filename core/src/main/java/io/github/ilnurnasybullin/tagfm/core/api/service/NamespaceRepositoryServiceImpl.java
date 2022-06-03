package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRepositoryService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.model.namespace.Namespace;
import io.github.ilnurnasybullin.tagfm.core.model.namespace.NamespaceMapper;
import io.github.ilnurnasybullin.tagfm.core.model.namespace.NamespaceRepoDto;
import io.github.ilnurnasybullin.tagfm.core.model.namespace.NamespaceSafety;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceEntity;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
public class NamespaceRepositoryServiceImpl implements NamespaceRepositoryService<NamespaceView> {

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
    public NamespaceView init(String name, FileNamingStrategy strategy) {
        find(name).ifPresent(namespace -> {
            throw new NamespaceAlreadyInitializedException(String.format("Namespace [%s] already initialized!", namespace.name()));
        });

        return NamespaceSafety.init(name, strategy);
    }

    @Override
    public Optional<NamespaceView> find(String name) {
        Function<NamespaceEntity, NamespaceView> mapper = new NamespaceMapper();
        return repository.findBy(name)
                .map(mapper);
    }

    @Override
    public List<NamespaceView> getAll() {
        Function<NamespaceEntity, NamespaceView> mapper = new NamespaceMapper();
        return repository.getAll()
                .stream()
                .map(mapper)
                .toList();
    }

    @Override
    public void commit(NamespaceView namespace) {
        repository.commit(NamespaceRepoDto.of(namespace));
    }

    @Override
    public void replace(String name, NamespaceView replacingNamespace) {
        find(name).ifPresent(namespace -> {
            throw new NamespaceAlreadyInitializedException(
                    String.format("Namespace [%s] is already exist!", namespace.name())
            );
        });

        repository.replace(name, NamespaceRepoDto.of(replacingNamespace));
    }

    @Override
    public void remove(NamespaceView namespace) {
        repository.remove(NamespaceRepoDto.of(namespace));
    }

    @Override
    public Optional<NamespaceView> getWorkingNamespace() {
        Function<NamespaceEntity, NamespaceView> mapper = new NamespaceMapper();
        return repository.getWorkingNamespace()
                .map(mapper);
    }

    @Override
    public void setWorkingNamespace(String name) {
        repository.setWorkingNamespace(name);
    }
}
