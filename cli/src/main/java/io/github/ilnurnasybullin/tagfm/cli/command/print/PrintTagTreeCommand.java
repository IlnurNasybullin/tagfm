package io.github.ilnurnasybullin.tagfm.cli.command.print;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.format.TreeTagPrinter;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceTagSearcher;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.Optional;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "tag-tree")
public class PrintTagTreeCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Parameters(arity = "0", index = "0")
    private Optional<String> rootTag;

    public PrintTagTreeCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        TreeTagDto root = rootTag.map(tag -> getTag(namespace, tag)).orElse(getRootTag(namespace));
        TreeTagPrinter printer = new TreeTagPrinter(root);
        printer.print();
    }

    private TreeTagDto getTag(NamespaceDto namespace, String tagName) {
        return new NamespaceTagSearcherFacade().searchTag(tagName, namespace, shortName);
    }

    private TreeTagDto getRootTag(NamespaceDto namespace) {
        return NamespaceTagSearcher.of(namespace).root();
    }
}
