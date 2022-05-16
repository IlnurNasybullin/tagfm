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

class RebaseNewTagParentBinder implements TagParentBinder {

    private final NamespaceDto namespace;

    private RebaseNewTagParentBinder(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static RebaseNewTagParentBinder of(NamespaceDto namespace) {
        return new RebaseNewTagParentBinder(namespace);
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
        TreeTagDto removingTag = leafs.get(tagName);
        walker.walk(removingTag, tag);

        namespace.synonymsManager().unbind(removingTag);
        namespace.fileManager().removeTag(removingTag);

        removingTag.reparent(null);
        tag.reparent(parent);
    }

    private void noCollision(TreeTagDto primaryChild, TreeTagDto noCollisionParent) {
        primaryChild.reparent(noCollisionParent);
    }

    private void hasCollision(TreeTagDto primaryChildTag, TreeTagDto collisionChildTag) {
        namespace.synonymsManager().unbind(primaryChildTag);
        namespace.fileManager().removeTag(primaryChildTag);
        primaryChildTag.reparent(null);
    }
}
