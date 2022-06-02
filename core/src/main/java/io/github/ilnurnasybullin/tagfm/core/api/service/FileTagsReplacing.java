package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.FileTagsReplacingService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFile;

import java.nio.file.Path;

/**
 * @author Ilnur Nasybullin
 */
public class FileTagsReplacing implements FileTagsReplacingService<TaggedFileView> {

    private final NamespaceView namespace;

    private FileTagsReplacing(NamespaceView namespace) {
        this.namespace = namespace;
    }

    public static FileTagsReplacing of(NamespaceView namespace) {
        return new FileTagsReplacing(namespace);
    }

    @Override
    public void replace(TaggedFileView file, Path newPath) {
        FileManager.of(namespace).find(newPath).ifPresent(f -> {
            throw new NamespaceAlreadyExistTaggedFileException(
                    String.format("File or dir [%s] is already existed in namespace [%s]!", f, namespace.name())
            );
        });

        replace((TaggedFile) file, newPath);
    }

    private void replace(TaggedFile file, Path newPath) {
        file.replace(newPath);
    }
}
