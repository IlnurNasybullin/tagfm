package io.github.ilnurnasybullin.tagfm.api.service;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Ilnur Nasybullin
 */
public interface FileFinderManagerService<TF> {
    TF findOrCreate(Path file);
    Map<Path, TF> findOrCreate(Collection<Path> paths);
    Map<Path, TF> find(Collection<Path> files);
    Optional<TF> find(Path file);
}
