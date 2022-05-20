package io.github.ilnurnasybullin.tagfm.api.service;

import java.nio.file.Path;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface FileReplacingService<TF> {
    void replace(TF file, Path newPath);
}
