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
