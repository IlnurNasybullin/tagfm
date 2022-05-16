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
import java.util.Map;
import java.util.function.BiConsumer;

public class CollisionWalker {

    private final BiConsumer<TreeTagDto, TreeTagDto> hasCollision;
    private final BiConsumer<TreeTagDto, TreeTagDto> noCollision;

    private CollisionWalker(BiConsumer<TreeTagDto, TreeTagDto> hasCollision, BiConsumer<TreeTagDto, TreeTagDto> noCollision) {
        this.hasCollision = hasCollision;
        this.noCollision = noCollision;
    }

    public static CollisionWalker of(BiConsumer<TreeTagDto, TreeTagDto> hasCollision,
                                     BiConsumer<TreeTagDto, TreeTagDto> noCollision) {
        return new CollisionWalker(hasCollision, noCollision);
    }

    public void walk(TreeTagDto primaryTag, TreeTagDto collisionTag) {
        ArrayDeque<TreeTagDto> primaryDeque = new ArrayDeque<>();
        primaryDeque.add(primaryTag);

        ArrayDeque<TreeTagDto> collisionDeque = new ArrayDeque<>();
        collisionDeque.add(collisionTag);

        TreeTagDto tag;
        TreeTagDto[] collision = new TreeTagDto[1];
        while (!primaryDeque.isEmpty()) {
            tag = primaryDeque.poll();
            collision[0] = collisionDeque.poll();

            Map<String, TreeTagDto> leafs = collision[0].children();
            Map.copyOf(tag.children()).forEach((name, primaryChild) -> {
                if (leafs.containsKey(name)) {
                    primaryDeque.add(primaryChild);
                    TreeTagDto collisionChild = leafs.get(name);
                    collisionDeque.add(collisionChild);

                    hasCollision.accept(primaryChild, collisionChild);
                } else {
                    noCollision.accept(primaryChild, collision[0]);
                }
            });
        }
    }
}
