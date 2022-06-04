package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.List;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface FileSearchingService<TF> {
    Set<TF> searchFiles(String expression, FileSearchStrategy searchStrategy);
}
