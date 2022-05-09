package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class NamespaceFileManager {

    public TaggedFileDto findOrCreate(Path path, NamespaceDto namespace) throws IOException {
        Path realPath = path.toRealPath();
        return namespace.files()
                .stream()
                .filter(file -> Objects.equals(file.file(), realPath))
                .findAny()
                .orElseGet(() -> TaggedFile.init(path));
    }

}
