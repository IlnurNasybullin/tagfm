/*
 * Copyright 2022 Ilnur Nasybullin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
