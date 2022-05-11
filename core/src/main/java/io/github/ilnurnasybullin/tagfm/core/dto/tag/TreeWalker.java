package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TreeWalker<T> {

    private final ArrayDeque<T> elements;
    private final Function<T, Collection<T>> childrenFunction;
    private final Predicate<T> hasNext;
    private final Consumer<T> handler;

    private TreeWalker(Function<T, Collection<T>> childrenFunction, Predicate<T> hasNext, Consumer<T> handler) {
        elements = new ArrayDeque<>();
        this.childrenFunction = childrenFunction;
        this.hasNext = hasNext;
        this.handler = handler;
    }

    public static <T> TreeWalker<T> of(Function<T, Collection<T>> childrenFunction, Predicate<T> hasNext,
                                    Consumer<T> handler) {
        return new TreeWalker<>(childrenFunction, hasNext, handler);
    }

    public void walk(T root) {
        elements.add(root);
        while (!elements.isEmpty()) {
            T element = elements.poll();
            handler.accept(element);
            if (hasNext.test(element)) {
                elements.addAll(childrenFunction.apply(element));
            }
        }
    }
}
