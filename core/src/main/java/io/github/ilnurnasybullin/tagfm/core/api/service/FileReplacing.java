package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.FileReplacingService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;

import java.nio.file.Path;

/**
 * @author Ilnur Nasybullin
 */
public class FileReplacing implements FileReplacingService<TaggedFile> {

    private final Namespace namespace;

    private FileReplacing(Namespace namespace) {
        this.namespace = namespace;
    }

    public static FileReplacing of(Namespace namespace) {
        return new FileReplacing(namespace);
    }

    @Override
    public void replace(TaggedFile file, Path newPath) {
        FileFinderManager.of(namespace).find(newPath).ifPresent(f -> {
            throw new NamespaceAlreadyExistTaggedFileException(
                    String.format("File or dir [%s] is already existed in namespace [%s]!", f, namespace.name())
            );
        });

        replace((io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFile) file, newPath);
    }

    private void replace(io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFile file, Path newPath) {
        file.replace(newPath);
    }
}
