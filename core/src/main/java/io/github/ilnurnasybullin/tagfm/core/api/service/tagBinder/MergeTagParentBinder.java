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

package io.github.ilnurnasybullin.tagfm.core.api.service.tagBinder;

import io.github.ilnurnasybullin.tagfm.api.service.FilesTagManagerService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FilesTagManager;
import io.github.ilnurnasybullin.tagfm.core.api.service.util.CollisionWalker;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.util.Map;

public class MergeTagParentBinder implements TagParentBinder {

    private final NamespaceView namespace;
    private final FilesTagManagerService<TagView> fileTagsManager;

    private MergeTagParentBinder(NamespaceView namespace, FilesTagManagerService<TagView> fileTagsManager) {
        this.namespace = namespace;
        this.fileTagsManager = fileTagsManager;
    }

    public static MergeTagParentBinder of(NamespaceView namespace) {
        return new MergeTagParentBinder(namespace, FilesTagManager.of(namespace));
    }

    @Override
    public void bindParent(TreeTag tag, TreeTag parent) {
        Map<String, TreeTag> leafs = parent.children();
        String tagName = tag.name();
        if (!leafs.containsKey(tagName)) {
            tag.reparent(parent);
            return;
        }

        CollisionWalker<TreeTag> walker = CollisionWalker.of(this::hasCollision, this::noCollision);
        TreeTag collisionTag = leafs.get(tagName);
        walker.walk(tag, collisionTag);

        fileTagsManager.replaceTag(tag, collisionTag);
        namespace.synonymsManager().replace(tag, collisionTag);
        tag.reparent(null);
    }

    private void noCollision(TreeTag primaryChild, TreeTag noCollisionParent) {
        primaryChild.reparent(noCollisionParent);
    }

    private void hasCollision(TreeTag primaryChildTag, TreeTag collisionChildTag) {
        namespace.synonymsManager().replace(primaryChildTag, collisionChildTag);
        fileTagsManager.replaceTag(primaryChildTag, collisionChildTag);
        primaryChildTag.reparent(null);
    }
}
