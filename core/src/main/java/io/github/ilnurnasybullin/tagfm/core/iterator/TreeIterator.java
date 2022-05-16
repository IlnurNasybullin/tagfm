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
