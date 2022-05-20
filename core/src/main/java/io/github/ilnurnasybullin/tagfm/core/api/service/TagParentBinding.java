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

import io.github.ilnurnasybullin.tagfm.api.service.TagParentBindingService;
import io.github.ilnurnasybullin.tagfm.api.service.TagParentBindingStrategy;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.service.tagBinder.*;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

public class TagParentBinding implements TagParentBindingService<Tag> {

    private final Namespace namespace;

    private TagParentBinding(Namespace namespace) {
        this.namespace = namespace;
    }

    public static TagParentBinding of(Namespace namespace) {
        return new TagParentBinding(namespace);
    }

    @Override
    public void bindParent(Tag tag, Tag parent, TagParentBindingStrategy strategy) {
        TagParentBinder binder = switch (strategy) {
            case THROW_IF_COLLISION -> new ThrowIfCollisionTagParentBinder();
            case REBASE_OLD -> RebaseOldTagParentBinder.of(namespace);
            case REBASE_NEW -> RebaseNewTagParentBinder.of(namespace);
            case MERGE -> MergeTagParentBinder.of(namespace);
        };
        binder.bindParent((TreeTag) tag, (TreeTag) parent);
    }

    @Override
    public void unbind(Tag tag, TagParentBindingStrategy strategy) {
        Tag root = namespace.root();
        bindParent(tag, root, strategy);
    }

}