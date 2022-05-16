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

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

public class NamespaceTagRemover {

    private final NamespaceDto namespace;

    private NamespaceTagRemover(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static NamespaceTagRemover of(NamespaceDto namespace) {
        return new NamespaceTagRemover(namespace);
    }

    public void removeTag(TreeTagDto tag, TagRemovingStrategy strategy) {
        if (tag.parent().isEmpty()) {
            throw new IllegalTagForRemoving(
                    String.format("Tag [%s] for removing must have a parent!", tag.name())
            );
        }

        TagRemover remover = instanceRemover(strategy);
        remover.removeTag(tag);
    }

    private TagRemover instanceRemover(TagRemovingStrategy strategy) {
        return switch (strategy) {
            case UP_CHILDREN_WITHOUT_CONFLICTS -> UpChildrenWithoutConflicts.of(namespace);
            case REMOVE_CHILDREN -> RemoveChildrenTagRemover.of(namespace);
            case UP_AND_REBASE_NEW -> UpAndRebaseNewTagRemover.of(namespace);
            case UP_AND_REBASE_OLD -> UpAndRebaseOldTagRemover.of(namespace);
            case UP_AND_MERGE_CHILDREN -> UpAndMergeTagRemover.of(namespace);
        };
    }

}
