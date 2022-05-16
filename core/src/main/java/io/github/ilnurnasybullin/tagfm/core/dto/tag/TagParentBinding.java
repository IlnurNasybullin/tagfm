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

package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.TagParentBindingStrategy;

import java.util.Iterator;

public class TagParentBinding {

    private final NamespaceDto namespace;

    private TagParentBinding(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static TagParentBinding of(NamespaceDto namespace) {
        return new TagParentBinding(namespace);
    }

    public void bindParent(TreeTagDto tag, TreeTagDto parent, TagParentBindingStrategy strategy) {
        TagParentBinder binder = switch (strategy) {
            case THROW_IF_COLLISION -> new ThrowIfCollisionTagParentBinder();
            case REBASE_OLD -> RebaseOldTagParentBinder.of(namespace);
            case REBASE_NEW -> RebaseNewTagParentBinder.of(namespace);
            case MERGE -> MergeTagParentBinder.of(namespace);
        };
        binder.bindParent(tag, parent);
    }

    public void unbind(TreeTagDto tag, TagParentBindingStrategy strategy) {
        Iterator<TreeTagDto> iterator = namespace.horizontalTraversal();
        if (!iterator.hasNext()) {
            throw new NamespaceNotExistTagsException(
                    String.format("Namespace [%s] hasn't tags!", namespace.name())
            );
        }

        TreeTagDto root = iterator.next().parent().orElseThrow();
        bindParent(tag, root, strategy);
    }

}
