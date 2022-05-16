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

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;

import java.util.Map;

class MergeTagParentBinder implements TagParentBinder {

    private final NamespaceDto namespace;

    private MergeTagParentBinder(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static MergeTagParentBinder of(NamespaceDto namespace) {
        return new MergeTagParentBinder(namespace);
    }

    @Override
    public void bindParent(TreeTagDto tag, TreeTagDto parent) {
        Map<String, TreeTagDto> leafs = parent.children();
        String tagName = tag.name();
        if (!leafs.containsKey(tagName)) {
            tag.reparent(parent);
            return;
        }

        CollisionWalker walker = CollisionWalker.of(this::hasCollision, this::noCollision);
        TreeTagDto collisionTag = leafs.get(tagName);
        walker.walk(tag, collisionTag);

        namespace.fileManager().replaceTag(tag, collisionTag);
        namespace.synonymsManager().replace(tag, collisionTag);
        tag.reparent(null);
    }

    private void noCollision(TreeTagDto primaryChild, TreeTagDto noCollisionParent) {
        primaryChild.reparent(noCollisionParent);
    }

    private void hasCollision(TreeTagDto primaryChildTag, TreeTagDto collisionChildTag) {
        namespace.synonymsManager().replace(primaryChildTag, collisionChildTag);
        namespace.fileManager().replaceTag(primaryChildTag, collisionChildTag);
        primaryChildTag.reparent(null);
    }
}
