package io.github.ilnurnasybullin.tagfm.cli.command.list;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "synonyms")
public class ListSynonymsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", index = "0")
    private String tagName;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    public ListSynonymsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        TreeTagDto tag = getTag(namespace);
        namespace.synonymsManager()
                .synonyms(tag)
                .stream()
                .filter(t -> t != tag)
                .map(TreeTagDto::fullName)
                .forEach(System.out::println);
    }

    private TreeTagDto getTag(NamespaceDto namespace) {
        return new NamespaceTagSearcherFacade().searchTag(tagName, namespace, shortName);
    }
}
