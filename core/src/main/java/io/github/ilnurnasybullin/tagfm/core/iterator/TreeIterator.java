package io.github.ilnurnasybullin.tagfm.core.iterator;

import java.util.*;
import java.util.function.Function;

public class TreeIterator<T> implements Iterator<T> {

    private final Deque<T> horizontalTraversal;
    private final Function<T, Collection<T>> childrenFunction;

    private TreeIterator(Deque<T> horizontalTraversal, Function<T, Collection<T>> childrenFunction) {
        this.horizontalTraversal = horizontalTraversal;
        this.childrenFunction = childrenFunction;
    }

    public static <T> TreeIterator<T> horizontalTraversal(T root, Function<T, Collection<T>> childrenFunction) {
        return new TreeIterator<>(new ArrayDeque<>(List.of(root)), childrenFunction);
    }

    @Override
    public boolean hasNext() {
        return !horizontalTraversal.isEmpty();
    }

    @Override
    public T next() {
        T element = horizontalTraversal.poll();
        if (element == null) {
            throw new NoSuchElementException("Iterator hasn't any element!");
        }

        horizontalTraversal.addAll(childrenFunction.apply(element));
        return element;
    }
}
