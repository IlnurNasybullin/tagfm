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

package io.github.ilnurnasybullin.tagfm.core.api.service.tagRemover;

import io.github.ilnurnasybullin.tagfm.api.service.FilesTagManagerService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.service.FilesTagManager;
import io.github.ilnurnasybullin.tagfm.core.api.service.util.CollisionWalker;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

public class UpAndRebaseOldTagRemover implements TagRemover {

    private final Namespace namespace;
    private final FilesTagManagerService<Tag> fileTagsManager;

    private UpAndRebaseOldTagRemover(Namespace namespace, FilesTagManagerService<Tag> fileTagsManager) {
        this.namespace = namespace;
        this.fileTagsManager = fileTagsManager;
    }

    public static UpAndRebaseOldTagRemover of(Namespace namespace) {
        return new UpAndRebaseOldTagRemover(namespace, FilesTagManager.of(namespace));
    }

    @Override
    public void removeTag(TreeTag tag) {
        TreeTag parent = tag.parent().orElseThrow();

        CollisionWalker<TreeTag> walker = CollisionWalker.of(this::hasCollision, this::noCollision);
        walker.walk(tag, parent);

        namespace.synonymsManager().unbind(tag);
        fileTagsManager.removeTag(tag);
        tag.reparent(null);
    }

    private void noCollision(TreeTag primaryChild, TreeTag collisionParent) {
        primaryChild.reparent(collisionParent);
    }

    private void hasCollision(TreeTag primaryChild, TreeTag collisionChild) {
        fileTagsManager.removeTag(primaryChild);
        namespace.synonymsManager().unbind(primaryChild);
        primaryChild.reparent(null);
    }
}