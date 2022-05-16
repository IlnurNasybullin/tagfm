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

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagCollisionException;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.Map;

class UpChildrenWithoutConflicts implements TagRemover {

    private final NamespaceDto namespace;

    private UpChildrenWithoutConflicts(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static UpChildrenWithoutConflicts of(NamespaceDto namespace) {
        return new UpChildrenWithoutConflicts(namespace);
    }

    @Override
    public void removeTag(TreeTagDto tag) {
        TreeTagDto parent = tag.parent().orElseThrow();
        checkOnUnique(tag, parent);
        Map.copyOf(tag.children()).values()
                .forEach(child -> child.reparent(parent));

        tag.reparent(null);

        namespace.fileManager().removeTag(tag);
        namespace.synonymsManager().unbind(tag);
    }

    private void checkOnUnique(TreeTagDto tag, TreeTagDto parent) {
        Map<String, TreeTagDto> leafs = parent.children();
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
