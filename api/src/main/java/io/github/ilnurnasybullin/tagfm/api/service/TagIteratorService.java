package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.Iterator;

/**
 * @author Ilnur Nasybullin
 */
public interface TagIteratorService<T> {
    Iterator<T> horizontalTraversal();
    Iterator<T> leveledIterator(int maxDepth);
}
