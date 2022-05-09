package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceAlreadyInitialized;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.io.Closeable;
import java.util.Optional;

@CommandLine.Command(name = "tagfm", subcommands = {
        NamespaceInitCommand.class,
        NamespaceCommand.class,
        AddTagsCommand.class,
        RenameTagCommand.class,
        RemoveTagCommand.class,
        BindCommand.class
})
@Singleton
public class FileManagerCommand implements Runnable, Closeable {

    private Optional<NamespaceDto> namespace;
    private final NamespaceServiceImpl namespaceService;

    private boolean onCommit = false;

    public FileManagerCommand(NamespaceServiceImpl namespaceService) {
        this.namespaceService = namespaceService;
    }

    @PostConstruct
    private void initNamespace() {
        initNamespace(namespaceService.find());
    }

    void initNamespace(Optional<NamespaceDto> namespace) {
        this.namespace = namespace;
    }

    void checkNamespaceOnNonExisting() {
        namespace.ifPresent(namespace -> {
            throw new NamespaceAlreadyInitialized(
                    String.format("Namespace [%s] has already initialized!", namespace.name())
            );
        });
    }

    NamespaceDto namespaceOrThrow() {
        return namespace.orElseThrow(() -> new NamespaceNotInitializedException("Namespace isn't initialized!"));
    }

    void setWriteMode() {
        onCommit = true;
    }

    @Override
    public void close() {
        if (onCommit) {
            namespace.ifPresent(namespaceService::commit);
        }
    }

    @Override
    public void run() {}
}
