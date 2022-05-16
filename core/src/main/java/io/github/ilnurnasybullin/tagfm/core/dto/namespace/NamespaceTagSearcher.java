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
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagSplitter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NamespaceTagSearcher {

    private final NamespaceDto namespace;

    private NamespaceTagSearcher(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static NamespaceTagSearcher of(NamespaceDto namespace) {
        return new NamespaceTagSearcher(namespace);
    }

    public TreeTagDto root() {
        return namespace.root();
    }

    public Optional<TreeTagDto> findByFullName(String fullName) {
        TreeTagDto tag = namespace.root();
        if (Objects.equals(tag.name(), fullName)) {
            throw new IllegalStateException(String.format("Illegal tag full [%s] name for searching!", fullName));
        }

        String[] tagNames = new TreeTagSplitter().tagNames(fullName);

        int i = 0;
        while (i < tagNames.length && tag != null) {
            tag = tag.children().get(tagNames[i]);
            i++;
        }

        return Optional.ofNullable(tag);
    }

    public Stream<TreeTagDto> findByName(String name) {
        return namespace.tags()
                .filter(tag -> Objects.equals(tag.name(), name));
    }

    public Map<String, List<TreeTagDto>> findByNames(Collection<String> names) {
        Set<String> setNames = Set.copyOf(names);
        return namespace.tags()
                .filter(tag -> setNames.contains(tag.name()))
                .collect(Collectors.groupingBy(TreeTagDto::name));
    }

    public Map<String, TreeTagDto> findByFullNames(Collection<String> names) {
        Set<String> setNames = Set.copyOf(names);
        return namespace.tags()
                .filter(tag -> setNames.contains(tag.fullName()))
                .collect(Collectors.toMap(TreeTagDto::fullName, Function.identity()));
    }

}
