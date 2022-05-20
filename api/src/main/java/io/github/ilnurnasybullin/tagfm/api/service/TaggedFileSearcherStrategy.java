package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.List;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface TaggedFileSearcherStrategy<TF> {
    Set<TF> searchFiles(List<String> expressionTokens, FileSearchStrategy searchStrategy);
}
