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

import java.util.Collection;
import java.util.Map;

public class NamespaceTagAdder {

    private final NamespaceDto namespace;

    private NamespaceTagAdder(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static NamespaceTagAdder of(NamespaceDto namespace) {
        return new NamespaceTagAdder(namespace);
    }

    public void addTags(Collection<TreeTagDto> tags) {
        tags.forEach(this::addTag);
    }

    public void addTag(TreeTagDto tag) {
        addTag(namespace.root(), tag);
    }

    public static void addTag(TreeTagDto parent, TreeTagDto child) {
        String childName = child.name();
        Map<String, TreeTagDto> leafs = parent.children();
        if (!leafs.containsKey(childName)) {
            child.reparent(parent);
            return;
        }

        TreeTagDto newParent = leafs.get(childName);
        Map.copyOf(child.children()).forEach((name, newChild) -> addTag(newParent, newChild));
    }

}
