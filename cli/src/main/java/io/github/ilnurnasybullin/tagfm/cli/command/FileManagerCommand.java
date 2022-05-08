package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.cli.format.TableFormatPrinter;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceAlreadyInitialized;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@CommandLine.Command(name = "tagfm", subcommands = {
        NamespaceInitCommand.class
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

    public void printInfo() {
        NamespaceDto namespace = namespaceOrThrow();

        printNamespace(namespace);
        printTags(namespace);
        printSynonyms(namespace);
    }

    private void printSynonyms(NamespaceDto namespace) {
        System.out.println("\nSynonym tags:");
        if (namespace.synonyms().isEmpty()) {
            System.out.println("Synonym tags are missing");
        }
    }

    private void printNamespace(NamespaceDto namespace) {
        System.out.println("Namespace info: ");
        TableFormatPrinter printer = new TableFormatPrinter("%-25s | %-35s | %-25s%n", 91);
        printer.print(new String[]{"name", "created", "file naming strategy"},
                new Object[][] {{namespace.name(), namespace.created(), namespace.fileNaming()}});
    }

    private void printTags(NamespaceDto namespace) {
        System.out.println("\nTags info:");
        if (namespace.<TreeTagDto>tags().noneMatch(tag -> true)) {
            System.out.println("Tags are missing");
            return;
        }

        TableFormatPrinter printer = new TableFormatPrinter("%-25s | %-25s%n", 53);
        printer.print(
                new String[]{"name", "parent"},
                namespace.tags(),
                List.<Function<TreeTagDto, Object>>of(TreeTagDto::name, tag -> tag.parent().map(TreeTagDto::name).orElse(""))
        );
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
