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
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.tagBinder.*;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

public class TagParentBinder implements TagParentBindingService<TagView> {

    private final NamespaceView namespace;

    private TagParentBinder(NamespaceView namespace) {
        this.namespace = namespace;
    }

    public static TagParentBinder of(NamespaceView namespace) {
        return new TagParentBinder(namespace);
    }

    @Override
    public void bind(TagView tag, TagView parent, TagParentBindingStrategy strategy) {
        InnerTagParentBinder binder = InnerTagParentBinder.instanceBinder(namespace, strategy);
        binder.bindParent((TreeTag) tag, (TreeTag) parent);
    }

    @Override
    public void unbind(TagView tag, TagParentBindingStrategy strategy) {
        TagView root = namespace.root();
        bind(tag, root, strategy);
    }

}
