package io.github.ilnurnasybullin.tagfm.cli.command.list;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceTagSearcher;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.core.iterator.TreeIteratorsFactory;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.*;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "tags")
public class ListTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(index = "0", arity = "0")
    private Optional<String> rootTag;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-d", "--depth"})
    private Optional<Integer> depth;

    public ListTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        TreeTagDto root = rootTag.map(tag -> getTag(namespace, tag)).orElse(getRootTag(namespace));
        Function<TreeTagDto, Collection<TreeTagDto>> leafsSupplier = tag -> new TreeMap<>(tag.children()).values();

        Iterator<TreeTagDto> iterator = depth.map(d ->
                        TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.LEVELED.iterator(root, leafsSupplier, d))
                        .orElse(TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE.iterator(root, leafsSupplier));

        iterator.next();
        iterator.forEachRemaining(tag -> System.out.println(tag.fullName()));
    }

    private TreeTagDto getTag(NamespaceDto namespace, String tagName) {
        return new NamespaceTagSearcherFacade().searchTag(tagName, namespace, shortName);
    }

    private TreeTagDto getRootTag(NamespaceDto namespace) {
        return NamespaceTagSearcher.of(namespace).root();
    }

}