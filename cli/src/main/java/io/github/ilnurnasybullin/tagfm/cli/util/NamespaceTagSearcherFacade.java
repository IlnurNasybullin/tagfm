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

package io.github.ilnurnasybullin.tagfm.cli.util;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceTagSearcher;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.Collection;
import java.util.stream.Stream;

public class NamespaceTagSearcherFacade {

    public Stream<TreeTagDto> searchTags(Collection<String> names, NamespaceDto namespace, boolean byShortName) {
        return names.stream()
                .map(name -> searchTag(name, namespace, byShortName));
    }

    public TreeTagDto searchTag(String name, NamespaceDto namespace, boolean byShortName) {
        NamespaceTagSearcher tagSearcher = NamespaceTagSearcher.of(namespace);

        if (!byShortName) {
            return tagSearcher.findByFullName(name).orElseThrow(() ->
                    tagNotFound(name, namespace)
            );
        }

        TreeTagDto[] tags = tagSearcher.findByName(name).toArray(TreeTagDto[]::new);
        if (tags.length == 0) {
            throw tagNotFound(name, namespace);
        }
        if (tags.length > 1) {
            throw new MultiplyTagSelectionException(String.format(
                    "Tag with short name [%s] is not unique in namespace [%s]!",
                    name, namespace.name()
            ));
        }

        return tags[0];
    }

    private IllegalArgumentException tagNotFound(String name, NamespaceDto namespace) {
        return new IllegalArgumentException(String.format(
                "Tag with name [%s] is not found in namespace [%s]!",
                name, namespace.name()
        ));
    }

}
