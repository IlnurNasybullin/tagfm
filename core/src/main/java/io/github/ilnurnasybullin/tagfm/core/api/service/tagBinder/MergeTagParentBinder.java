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
import io.github.ilnurnasybullin.tagfm.core.api.dto.SynonymTagManagerView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FilesTagManager;
import io.github.ilnurnasybullin.tagfm.core.api.service.util.CollisionWalker;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.util.Map;

public class MergeTagParentBinder implements InnerTagParentBinder {

    private final SynonymTagManagerView synonymsManager;
    private final FilesTagManagerService<TagView> fileTagsManager;

    private MergeTagParentBinder(SynonymTagManagerView synonymsManager, FilesTagManagerService<TagView> fileTagsManager) {
        this.synonymsManager = synonymsManager;
        this.fileTagsManager = fileTagsManager;
    }

    public static MergeTagParentBinder of(NamespaceView namespace) {
        return new MergeTagParentBinder(namespace.synonymsManager(), FilesTagManager.of(namespace));
    }

    @Override
    public void bindParent(TreeTag tag, TreeTag parent) {
        Map<String, TreeTag> leafs = parent.children();
        String tagName = tag.name();
        TreeTag collisionTag = leafs.get(tagName);
        if (collisionTag == null) {
            tag.reparent(parent);
            return;
        }

        CollisionWalker<TreeTag> walker = CollisionWalker.of(this::hasCollision, this::noCollision);
        walker.walk(tag, collisionTag);

        fileTagsManager.replaceTag(tag, collisionTag);
        synonymsManager.merge(tag, collisionTag);
        synonymsManager.unbind(collisionTag);
        tag.reparent(null);
    }

    private void noCollision(TreeTag primaryChild, TreeTag noCollisionParent) {
        primaryChild.reparent(noCollisionParent);
    }

    private void hasCollision(TreeTag primaryChildTag, TreeTag collisionChildTag) {
        synonymsManager.merge(primaryChildTag, collisionChildTag);
        synonymsManager.unbind(collisionChildTag);
        fileTagsManager.replaceTag(primaryChildTag, collisionChildTag);
        primaryChildTag.reparent(null);
    }
}
