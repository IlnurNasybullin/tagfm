package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.FileFinderManagerService;
import io.github.ilnurnasybullin.tagfm.api.service.TaggedFileNotFoundException;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFileSafety;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ilnur Nasybullin
 */
public class FileFinderManager implements FileFinderManagerService<TaggedFileView> {

    private final NamespaceView namespace;

    private FileFinderManager(NamespaceView namespace) {
        this.namespace = namespace;
    }

    public static FileFinderManager of(NamespaceView namespace) {
        return new FileFinderManager(namespace);
    }

    private Path toRealPath(Path file) {
        try {
            return file.toAbsolutePath().toRealPath();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Set<Path> toRealPaths(Collection<Path> files) {
        return files.stream()
                .map(this::toRealPath)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<TaggedFileView> find(Path file) {
        Path realPath = toRealPath(file);
        return namespace.files()
                .stream()
                .filter(taggedFile -> Objects.equals(taggedFile.file(), realPath))
                .findAny();
    }

    @Override
    public TaggedFileView findOrCreate(Path file) {
        Path realPath = toRealPath(file);
        return namespace.files()
                .stream()
                .filter(taggedFile -> Objects.equals(taggedFile.file(), realPath))
                .findAny()
                .orElseGet(() -> TaggedFileSafety.init(file));
    }

    private <T> Map<Path, T> removeFoundedFiles(Set<Path> realPaths, Function<TaggedFileView, T> valueFunction) {
        return namespace.files()
                .stream()
                .filter(file -> realPaths.remove(file.file()))
                .collect(Collectors.toMap(TaggedFileView::file, valueFunction));
    }

    @Override
    public Map<Path, Optional<TaggedFileView>> find(Collection<Path> files) {
        Set<Path> realPaths = toRealPaths(files);
        return removeFoundedFiles(realPaths, Optional::of);
    }

    @Override
    public Map<Path, TaggedFileView> findOrCreate(Collection<Path> paths) {
        Set<Path> realPaths = toRealPaths(paths);
        Map<Path, TaggedFileView> foundFiles = removeFoundedFiles(realPaths, Function.identity());
        realPaths.forEach(path -> foundFiles.put(path, TaggedFileSafety.init(path)));
        return foundFiles;
    }

    private TaggedFileNotFoundException notFound(Path file) {
        return new TaggedFileNotFoundException(
                String.format("File [%s] isn't found in namespace [%s]", file, namespace.name())
        );
    }

    @Override
    public TaggedFileView findExact(Path file) {
        return find(file).orElseThrow(() -> notFound(file));
    }

    @Override
    public Stream<TaggedFileView> findExact(Collection<Path> files) {
        Map<Path, Optional<TaggedFileView>> foundFiles = find(files);
        return files.stream()
                .map(file -> foundFiles.getOrDefault(file, Optional.empty())
                        .orElseThrow(() -> notFound(file))
                );
    }
}
