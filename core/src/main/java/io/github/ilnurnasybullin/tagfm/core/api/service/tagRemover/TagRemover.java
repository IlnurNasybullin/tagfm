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

import io.github.ilnurnasybullin.tagfm.api.service.TagRemovingStrategy;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

public interface TagRemover {
    void removeTag(TreeTag tag);

    static TagRemover instanceRemover(TagRemovingStrategy strategy, Namespace namespace) {
        return switch (strategy) {
            case UP_CHILDREN_WITHOUT_CONFLICTS -> UpChildrenWithoutConflicts.of(namespace);
            case REMOVE_CHILDREN -> RemoveChildrenTagRemover.of(namespace);
            case UP_AND_REBASE_NEW -> UpAndRebaseNewTagRemover.of(namespace);
            case UP_AND_REBASE_OLD -> UpAndRebaseOldTagRemover.of(namespace);
            case UP_AND_MERGE_CHILDREN -> UpAndMergeTagRemover.of(namespace);
        };
    }
}