package io.github.ilnurnasybullin.tagfm.cli.command.bind;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;

@Singleton
@CommandLine.Command(name = "synonyms")
public class BindSynonymsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1")
    private final List<String> tags = new ArrayList<>();

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    public BindSynonymsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        List<TreeTagDto> tags = getTags(namespace);
        if (tags.size() < 2) {
            return;
        }

        bindSynonyms(tags, namespace);
        fileManager.setWriteMode();
    }

    private void bindSynonyms(List<TreeTagDto> tags, NamespaceDto namespace) {
        namespace.synonymsManager().bind(tags);
    }

    private List<TreeTagDto> getTags(NamespaceDto namespace) {
        return new NamespaceTagSearcherFacade()
                .searchTags(tags, namespace, shortName)
                .toList();
    }
}
