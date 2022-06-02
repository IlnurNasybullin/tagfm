package io.github.ilnurnasybullin.tagfm.api.service;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ilnur Nasybullin
 */
public interface FileFinderManagerService<TF> {
    TF findOrCreate(Path file);
    Map<Path, TF> findOrCreate(Collection<Path> paths);

    Optional<TF> find(Path file);
    Map<Path, Optional<TF>> find(Collection<Path> files);

    private TaggedFileNotFoundException notFound(Path file) {
        return new TaggedFileNotFoundException(String.format("File [%s] isn't found in namespace!", file));
    }

    default TF findExact(Path file) {
        return find(file).orElseThrow(
                () -> notFound(file)
        );
    }

    default Stream<TF> findExact(Collection<Path> files) {
        Map<Path, Optional<TF>> foundFiles = find(files);
        return files.stream()
                .map(file -> foundFiles.getOrDefault(file, Optional.empty())
                        .orElseThrow(() -> notFound(file))
                );
    }
}
