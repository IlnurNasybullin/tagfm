package io.github.ilnurnasybullin.tagfm.cli.util;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceFileManager;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceNotExistTaggedFileException;

import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

public class NamespaceFileManagerFacade {

    public Stream<TaggedFileDto> findOrCreate(Collection<Path> files, NamespaceDto namespace) {
        return NamespaceFileManager.of(namespace)
                .findOrCreate(files)
                .values()
                .stream();
    }

    public TaggedFileDto findOrCreate(Path file, NamespaceDto namespace) {
        return NamespaceFileManager.of(namespace)
                .findOrCreate(file);
    }


    public Stream<TaggedFileDto> find(Collection<Path> files, NamespaceDto namespace) {
        return NamespaceFileManager.of(namespace)
                .find(files)
                .values()
                .stream();
    }
    
    public TaggedFileDto findExact(Path file, NamespaceDto namespace) {
        return NamespaceFileManager.of(namespace)
                .find(file)
                .orElseThrow(() ->
                        new NamespaceNotExistTaggedFileException(
                                String.format("File or directory [%s] is not exist in namespace [%s]",
                                        file, namespace.name()
                                )
                        )
                );
    }
}
