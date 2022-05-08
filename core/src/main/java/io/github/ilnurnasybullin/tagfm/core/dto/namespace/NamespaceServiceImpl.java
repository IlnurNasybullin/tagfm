package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceService;
import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class NamespaceServiceImpl implements NamespaceService<NamespaceDto> {

    public static final String DIR_NAME = ".tagfm";
    public static final String FILE_NAME = "namespace.xml";
    private final NamespaceRepository repository;

    public NamespaceServiceImpl(NamespaceRepository repository) {
        this.repository = repository;
    }

    public NamespaceServiceImpl() {
        this(NamespaceRepository.get());
    }

    public void commit(NamespaceDto namespace) {
        repository.commit(namespace, storingPath());
    }

    public Optional<NamespaceDto> find() {
        Path path = storingPath();
        if (Files.notExists(path) || Files.isDirectory(path) || !Files.isReadable(path)) {
            return Optional.empty();
        }

        return repository.findBy(path)
                .map(Namespace::from);
    }

    private Path storingPath() {
        return Path.of(".", DIR_NAME, FILE_NAME);
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
