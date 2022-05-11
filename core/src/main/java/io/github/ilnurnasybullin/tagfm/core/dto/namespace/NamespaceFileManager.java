package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NamespaceFileManager {

    public TaggedFileDto findOrCreate(Path path, NamespaceDto namespace) throws IOException {
        Path realPath = path.toRealPath();
        return namespace.files()
                .stream()
                .filter(file -> Objects.equals(file.file(), realPath))
                .findAny()
                .orElseGet(() -> TaggedFile.init(path));
    }

    public Map<Path, TaggedFileDto> findOrCreate(Collection<Path> paths, NamespaceDto namespace) {
        Set<Path> realPaths = toRealPaths(paths);

        Map<Path, TaggedFileDto> foundFiles = foundFiles(namespace, realPaths);

        realPaths.stream()
                .map(path -> Map.entry(path, TaggedFile.init(path)))
                .forEach(entry -> foundFiles.put(entry.getKey(), entry.getValue()));

        return foundFiles;
    }

    public Map<Path, TaggedFileDto> find(Collection<Path> files, NamespaceDto namespace) {
        Set<Path> realPaths = toRealPaths(files);

        Map<Path, TaggedFileDto> foundFiles = foundFiles(namespace, realPaths);

        if (!realPaths.isEmpty()) {
            throw new NamespaceNotExistTaggedFile(
                    String.format("Files [%s] is not existing in namespace [%s]!", realPaths, namespace.name())
            );
        }

        return foundFiles;
    }

    private Map<Path, TaggedFileDto> foundFiles(NamespaceDto namespace, Set<Path> realPaths) {
        return namespace.files()
                .stream()
                .filter(file -> realPaths.remove(file.file()))
                .collect(Collectors.toMap(TaggedFileDto::file, Function.identity()));
    }

    private Set<Path> toRealPaths(Collection<Path> files) {
        return files.stream().map(path -> {
            try {
                return path.toRealPath();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }).collect(Collectors.toSet());
    }
}
