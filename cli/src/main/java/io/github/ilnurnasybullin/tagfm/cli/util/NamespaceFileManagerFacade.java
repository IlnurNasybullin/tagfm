package io.github.ilnurnasybullin.tagfm.cli.util;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceFileManager;

import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

public class NamespaceFileManagerFacade {

    public Stream<TaggedFileDto> findOrCreate(Collection<Path> files, NamespaceDto namespace) {
        return new NamespaceFileManager()
                .findOrCreate(files, namespace)
                .values()
                .stream();
    }

}
