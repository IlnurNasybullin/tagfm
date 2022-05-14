package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NamespaceFileManager {

    private final NamespaceDto namespace;

    private NamespaceFileManager(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static NamespaceFileManager of(NamespaceDto namespace) {
        return new NamespaceFileManager(namespace);
    }

    public TaggedFileDto findOrCreate(Path file)  {
        Path realPath = toRealPath(file);
        return namespace.files()
                .stream()
                .filter(taggedFile -> Objects.equals(taggedFile.file(), realPath))
                .findAny()
                .orElseGet(() -> TaggedFile.init(file));
    }

    public Map<Path, TaggedFileDto> findOrCreate(Collection<Path> paths) {
        Set<Path> realPaths = toRealPaths(paths);

        Map<Path, TaggedFileDto> foundFiles = foundFiles(realPaths);

        realPaths.stream()
                .map(path -> Map.entry(path, TaggedFile.init(path)))
                .forEach(entry -> foundFiles.put(entry.getKey(), entry.getValue()));

        return foundFiles;
    }

    public Map<Path, TaggedFileDto> find(Collection<Path> files) {
        Set<Path> realPaths = toRealPaths(files);

        Map<Path, TaggedFileDto> foundFiles = foundFiles(realPaths);

        if (!realPaths.isEmpty()) {
            throw new NamespaceNotExistTaggedFileException(
                    String.format("Files [%s] is not existing in namespace [%s]!", realPaths, namespace.name())
            );
        }

        return foundFiles;
    }

    private Map<Path, TaggedFileDto> foundFiles(Set<Path> realPaths) {
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

    public Optional<TaggedFileDto> find(Path file) {
        Path realPath = toRealPath(file);
        return namespace.files()
                .stream()
                .filter(taggedFile -> Objects.equals(taggedFile.file(), realPath))
                .findAny();
    }

    private Path toRealPath(Path file) {
        try {
            return file.toAbsolutePath().toRealPath();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
