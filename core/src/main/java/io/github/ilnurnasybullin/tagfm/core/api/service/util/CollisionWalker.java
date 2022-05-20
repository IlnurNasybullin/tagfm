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

package io.github.ilnurnasybullin.tagfm.core.api.service.util;

import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class CollisionWalker<T extends Tag> {

    private final BiConsumer<T, T> hasCollision;
    private final BiConsumer<T, T> noCollision;

    private CollisionWalker(BiConsumer<T, T> hasCollision, BiConsumer<T, T> noCollision) {
        this.hasCollision = hasCollision;
        this.noCollision = noCollision;
    }

    public static <T extends Tag> CollisionWalker<T> of(BiConsumer<T, T> hasCollision,
                                            BiConsumer<T, T> noCollision) {
        return new CollisionWalker<>(hasCollision, noCollision);
    }

    public void walk(T primaryTag, T collisionTag) {
        ArrayDeque<T> primaryDeque = new ArrayDeque<>();
        primaryDeque.add(primaryTag);

        ArrayDeque<T> collisionDeque = new ArrayDeque<>();
        collisionDeque.add(collisionTag);

        T tag;
        AtomicReference<T> collision = new AtomicReference<>();
        while (!primaryDeque.isEmpty()) {
            tag = primaryDeque.poll();
            collision.set(collisionDeque.poll());

            Map<String, T> leafs = collision.get().children();
            Map.<String, T>copyOf(tag.children()).forEach((name, primaryChild) -> {
                if (leafs.containsKey(name)) {
                    primaryDeque.add(primaryChild);
                    T collisionChild = leafs.get(name);
                    collisionDeque.add(collisionChild);

                    hasCollision.accept(primaryChild, collisionChild);
                } else {
                    noCollision.accept(primaryChild, collision.get());
                }
            });
        }
    }
}
