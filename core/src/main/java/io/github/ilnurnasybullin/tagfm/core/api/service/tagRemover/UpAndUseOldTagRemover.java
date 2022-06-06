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
import io.github.ilnurnasybullin.tagfm.api.service.TagParentBindingService;
import io.github.ilnurnasybullin.tagfm.api.service.TagParentBindingStrategy;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FilesTagManager;
import io.github.ilnurnasybullin.tagfm.core.api.service.IllegalTagForRemovingException;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagParentBinder;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

public class UpAndUseOldTagRemover implements InnerNamespaceTagRemover {

    private final NamespaceView namespace;
    private final FilesTagManagerService<TagView> fileTagsManager;

    private UpAndUseOldTagRemover(NamespaceView namespace, FilesTagManagerService<TagView> fileTagsManager) {
        this.namespace = namespace;
        this.fileTagsManager = fileTagsManager;
    }

    public static UpAndUseOldTagRemover of(NamespaceView namespace) {
        return new UpAndUseOldTagRemover(namespace, FilesTagManager.of(namespace));
    }

    @Override
    public void removeTag(TreeTag tag) {
        TreeTag parent = tag.parent().orElseThrow(() -> new IllegalTagForRemovingException(
                String.format("Tag [%s] is illegal for removing - this tag hasn't parent", tag.fullName())
        ));

        TagParentBindingService<TagView> parentBinder = TagParentBinder.of(namespace);
        tag.children().forEach((childTagName, childTag) ->
                parentBinder.bind(childTag, parent, TagParentBindingStrategy.USE_OLD)
        );

        namespace.synonymsManager().unbind(tag);
        fileTagsManager.removeTag(tag);
        tag.reparent(null);
    }
}
