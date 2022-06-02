package io.github.ilnurnasybullin.tagfm.cli.command.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRepositoryService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "remove")
public class NamespaceRemoveCommand implements Runnable {

    private final NamespaceRepositoryService<NamespaceView> namespaceRepository;

    @CommandLine.Parameters(index = "0", arity = "1")
    private String namespaceName;

    public NamespaceRemoveCommand(NamespaceRepositoryService<NamespaceView> namespaceRepository) {
        this.namespaceRepository = namespaceRepository;
    }

    @Override
    public void run() {
        NamespaceView namespace = namespaceRepository.find(namespaceName).orElseThrow(() ->
                new NamespaceNotFoundException(String.format("Namespace with name [%s] isn't founded!", namespaceName))
        );
        namespaceRepository.remove(namespace);
    }
}
