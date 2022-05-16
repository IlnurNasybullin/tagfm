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

package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.CollisionWalker;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

class UpAndRebaseOldTagRemover implements TagRemover {

    private final NamespaceDto namespace;

    private UpAndRebaseOldTagRemover(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static UpAndRebaseOldTagRemover of(NamespaceDto namespace) {
        return new UpAndRebaseOldTagRemover(namespace);
    }

    @Override
    public void removeTag(TreeTagDto tag) {
        TreeTagDto parent = tag.parent().orElseThrow();

        CollisionWalker walker = CollisionWalker.of(this::hasCollision, this::noCollision);
        walker.walk(tag, parent);

        namespace.synonymsManager().unbind(tag);
        namespace.fileManager().removeTag(tag);
        tag.reparent(null);
    }

    private void noCollision(TreeTagDto primaryChild, TreeTagDto collisionParent) {
        primaryChild.reparent(collisionParent);
    }

    private void hasCollision(TreeTagDto primaryChild, TreeTagDto collisionChild) {
        namespace.fileManager().removeTag(primaryChild);
        namespace.synonymsManager().unbind(primaryChild);
        primaryChild.reparent(null);
    }
}
