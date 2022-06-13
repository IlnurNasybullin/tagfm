package io.github.ilnurnasybullin.tagfm.cli.command.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRepositoryService;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(
        name = "remove",
        headerHeading = "Usage:%n%n",
        header = "Namespace removing",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "removing namespace by name"
)
public class NamespaceRemoveCommand implements Runnable {

    private final NamespaceRepositoryService<NamespaceView> namespaceRepository;

    @CommandLine.Parameters(index = "0", arity = "1", description = "namespace for removing")
    private String namespaceName;

    @CommandLine.Mixin
    private HelpOption helper;

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
