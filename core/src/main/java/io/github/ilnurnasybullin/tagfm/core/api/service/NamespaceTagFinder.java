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

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceTagFinderService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.service.util.TreeTagSplitter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NamespaceTagFinder implements NamespaceTagFinderService<Tag> {

    private final Namespace namespace;

    private NamespaceTagFinder(Namespace namespace) {
        this.namespace = namespace;
    }

    public static NamespaceTagFinder of(Namespace namespace) {
        return new NamespaceTagFinder(namespace);
    }

    @Override
    public Optional<Tag> findByFullName(String fullName) {
        Tag tag = namespace.root();
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

    @Override
    public Stream<Tag> findByName(String name) {
        return namespace.tags(false)
                .filter(tag -> Objects.equals(tag.name(), name));
    }

    @Override
    public Map<String, List<Tag>> findByNames(Collection<String> names) {
        Set<String> setNames = Set.copyOf(names);
        return namespace.tags(false)
                .filter(tag -> setNames.contains(tag.name()))
                .collect(Collectors.groupingBy(Tag::name));
    }

    @Override
    public Map<String, Tag> findByFullNames(Collection<String> names) {
        Set<String> setNames = Set.copyOf(names);
        return namespace.tags(false)
                .filter(tag -> setNames.contains(tag.fullName()))
                .collect(Collectors.toMap(Tag::fullName, Function.identity()));
    }

}
