package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.FileReplacingService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFile;

import java.nio.file.Path;

/**
 * @author Ilnur Nasybullin
 */
public class FileReplacing implements FileReplacingService<TaggedFileView> {

    private final NamespaceView namespace;

    private FileReplacing(NamespaceView namespace) {
        this.namespace = namespace;
    }

    public static FileReplacing of(NamespaceView namespace) {
        return new FileReplacing(namespace);
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
