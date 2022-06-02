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

package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceTagRemovingService;
import io.github.ilnurnasybullin.tagfm.api.service.TagRemovingStrategy;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.tagRemover.TagRemover;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

public class NamespaceTagRemoving implements NamespaceTagRemovingService<TagView> {

    private final NamespaceView namespace;

    private NamespaceTagRemoving(NamespaceView namespace) {
        this.namespace = namespace;
    }

    public static NamespaceTagRemoving of(NamespaceView namespace) {
        return new NamespaceTagRemoving(namespace);
    }

    @Override
    public void removeTag(TagView tag, TagRemovingStrategy strategy) {
        if (tag.parent().isEmpty()) {
            throw new IllegalTagForRemovingException(
                    String.format("Tag [%s] for removing must have a parent!", tag.name())
            );
        }

        TagRemover remover = TagRemover.instanceRemover(strategy, namespace);
        remover.removeTag((TreeTag) tag);
    }

}
