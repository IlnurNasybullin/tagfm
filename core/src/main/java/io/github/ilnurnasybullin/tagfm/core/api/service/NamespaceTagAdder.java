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

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceTagAdderService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.util.Map;

public class NamespaceTagAdder implements NamespaceTagAdderService<TagView> {

    private final NamespaceView namespace;

    private NamespaceTagAdder(NamespaceView namespace) {
        this.namespace = namespace;
    }

    public static NamespaceTagAdder of(NamespaceView namespace) {
        return new NamespaceTagAdder(namespace);
    }

    @Override
    public void addTag(TagView tag) {
        addTag((TreeTag) namespace.root(), (TreeTag) tag);
    }

    private void addTag(TreeTag parent, TreeTag child) {
        String childName = child.name();
        Map<String, TreeTag> leafs = parent.children();
        if (!leafs.containsKey(childName)) {
            child.reparent(parent);
            return;
        }

        TreeTag newParent = leafs.get(childName);
        Map.copyOf(child.children()).forEach((name, newChild) -> addTag(newParent, newChild));
    }

}
