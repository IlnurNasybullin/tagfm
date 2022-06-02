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
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FilesTagManager;
import io.github.ilnurnasybullin.tagfm.core.api.service.TreeTagCollisionException;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.util.Map;

public class UpChildrenWithoutConflicts implements TagRemover {

    private final NamespaceView namespace;
    private final FilesTagManagerService<TagView> fileTagsManager;

    private UpChildrenWithoutConflicts(NamespaceView namespace, FilesTagManagerService<TagView> fileTagsManager) {
        this.namespace = namespace;
        this.fileTagsManager = fileTagsManager;
    }

    public static UpChildrenWithoutConflicts of(NamespaceView namespace) {
        return new UpChildrenWithoutConflicts(namespace, FilesTagManager.of(namespace));
    }

    @Override
    public void removeTag(TreeTag tag) {
        TreeTag parent = tag.parent().orElseThrow();
        checkOnUnique(tag, parent);
        Map.copyOf(tag.children()).values()
                .forEach(child -> child.reparent(parent));

        tag.reparent(null);

        fileTagsManager.removeTag(tag);
        namespace.synonymsManager().unbind(tag);
    }

    private void checkOnUnique(TreeTag tag, TreeTag parent) {
        Map<String, TreeTag> leafs = parent.children();
        tag.children()
                .keySet()
                .stream()
                .filter(leafs::containsKey)
                .findAny().ifPresent(name -> {
                    throw new TreeTagCollisionException(String.format(
                            "Child tag [%s] of removing tag [%s] already exist in parent tag [%s]!",
                            name, tag.name(), parent.name()
                    ));
                });
    }
}
