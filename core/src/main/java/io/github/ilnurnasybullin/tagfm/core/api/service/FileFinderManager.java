package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.FileFinderManagerService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFileSafety;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public class FileFinderManager implements FileFinderManagerService<TaggedFile> {

    private final Namespace namespace;

    private FileFinderManager(Namespace namespace) {
        this.namespace = namespace;
    }

    public static FileFinderManager of(Namespace namespace) {
        return new FileFinderManager(namespace);
    }

    private Path toRealPath(Path file) {
        try {
            return file.toAbsolutePath().toRealPath();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public TaggedFile findOrCreate(Path file) {
        Path realPath = toRealPath(file);
        return namespace.files()
                .stream()
                .filter(taggedFile -> Objects.equals(taggedFile.file(), realPath))
                .findAny()
                .orElseGet(() -> TaggedFileSafety.init(file));
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

    private Map<Path, TaggedFile> foundFiles(Set<Path> realPaths) {
        return namespace.files()
                .stream()
                .filter(file -> realPaths.remove(file.file()))
                .collect(Collectors.toMap(TaggedFile::file, Function.identity()));
    }

    @Override
    public Map<Path, TaggedFile> findOrCreate(Collection<Path> paths) {
        Set<Path> realPaths = toRealPaths(paths);

        Map<Path, TaggedFile> foundFiles = foundFiles(realPaths);

        realPaths.stream()
                .map(path -> Map.entry(path, TaggedFileSafety.init(path)))
                .forEach(entry -> foundFiles.put(entry.getKey(), entry.getValue()));

        return foundFiles;
    }

    @Override
    public Map<Path, TaggedFile> find(Collection<Path> files) {
        Set<Path> realPaths = toRealPaths(files);

        Map<Path, TaggedFile> foundFiles = foundFiles(realPaths);

        if (!realPaths.isEmpty()) {
            throw new NamespaceNotExistTaggedFileException(
                    String.format("Files [%s] is not existing in namespace [%s]!", realPaths, namespace.name())
            );
        }

        return foundFiles;
    }

    @Override
    public Optional<TaggedFile> find(Path file) {
        Path realPath = toRealPath(file);
        return namespace.files()
                .stream()
                .filter(taggedFile -> Objects.equals(taggedFile.file(), realPath))
                .findAny();
    }
}
