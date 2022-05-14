package io.github.ilnurnasybullin.tagfm.core.iterator;

import java.util.*;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
public class LevelTreeIterator<T> implements Iterator<T> {

    private Deque<T> mainDeque;
    private Deque<T> supportDeque;
    private final Function<T, Collection<T>> leafsSupplier;

    private int currentDepth;
    private final int maxDepth;

    private LevelTreeIterator(Deque<T> mainDeque, Function<T, Collection<T>> leafsSupplier, int maxDepth) {
        this.mainDeque = mainDeque;
        supportDeque = new ArrayDeque<>();
        this.leafsSupplier = leafsSupplier;
        this.maxDepth = maxDepth;
    }

    public static <T> LevelTreeIterator<T> horizontalTraversal(T root, Function<T, Collection<T>> leafsSupplier,
                                                               int maxDepth) {
        if (maxDepth < 0) {
            throw new NegativeTreeDepthException(
                    String.format("Incorrect argument - negative depth value [%d]!", maxDepth)
            );
        }
        return new LevelTreeIterator<>(new ArrayDeque<>(List.of(root)), leafsSupplier, maxDepth);
    }

    @Override
    public boolean hasNext() {
        return !emptyDeques() && currentDepth <= maxDepth;
    }

    private boolean emptyDeques() {
        return mainDeque.isEmpty() && supportDeque.isEmpty();
    }

    @Override
    public T next() {
        if (mainDeque.isEmpty()) {
            swapDeques();
            currentDepth++;
        }

        if (mainDeque.isEmpty()) {
            throw new NoSuchElementException("Elements aren't founded!");
        }

        T removed = mainDeque.removeFirst();

        if (currentDepth < maxDepth) {
            supportDeque.addAll(leafsSupplier.apply(removed));
        }

        return removed;
    }

    private void swapDeques() {
        Deque<T> tempDeque = mainDeque;
        mainDeque = supportDeque;
        supportDeque = tempDeque;
    }
}
