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

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceTagService;
import io.github.ilnurnasybullin.tagfm.api.service.NonUniqueTagNameException;
import io.github.ilnurnasybullin.tagfm.api.service.TagNotFoundException;
import io.github.ilnurnasybullin.tagfm.core.api.dto.InvalidTagNameException;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.util.TreeTagSplitter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagService implements NamespaceTagService<TagView> {

    private final NamespaceView namespace;

    private TagService(NamespaceView namespace) {
        this.namespace = namespace;
    }

    public static TagService of(NamespaceView namespace) {
        return new TagService(namespace);
    }

    @Override
    public Optional<TagView> findByFullName(String fullName) {
        TagView tag = namespace.root();
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
    public Stream<TagView> findByName(String name) {
        return namespace.tags(false)
                .filter(tag -> Objects.equals(tag.name(), name));
    }

    @Override
    public Map<String, List<TagView>> findByNames(Collection<String> names) {
        Set<String> setNames = Set.copyOf(names);
        return namespace.tags(false)
                .filter(tag -> setNames.contains(tag.name()))
                .collect(Collectors.groupingBy(TagView::name));
    }

    @Override
    public TagView findOrCreateByFullName(String fullName) {
        return findByFullName(fullName)
                .orElseGet(() -> new TagCreator().createByFullName(fullName));
    }

    @Override
    public TagView findOrCreateByNameExact(String name) {
        List<TagView> tags = findByName(name).toList();
        if (tags.isEmpty()) {
            return new TagCreator().createByFullName(name);
        }

        return getExactValue(name, tags);
    }

    @Override
    public Map<String, TagView> findOrCreateByFullNames(Collection<String> names) {
        Set<String> setNames = new HashSet<>(names);
        Map<String, TagView> tags = namespace.tags(false)
                .filter(tag -> setNames.contains(tag.fullName()))
                .collect(Collectors.toMap(TagView::fullName, Function.identity()));

        TagCreator tagCreator = new TagCreator();
        setNames.stream()
                .map(tagCreator::createByFullName)
                .forEach(tag -> tags.put(tag.fullName(), tag));

        return tags;
    }

    private InvalidTagNameException invalidTagName(String tagName) {
        return new InvalidTagNameException(
                String.format("Invalid full name [%s] for creating tag!", tagName)
        );
    }

    @Override
    public Map<String, TagView> findOrCreateByNamesExact(Collection<String> names) {
        Map<String, List<TagView>> foundTags = findByNames(names);
        TagCreator tagCreator = new TagCreator();
        return names.stream()
                .map(name -> getExactValue(name, foundTags.getOrDefault(
                        name,
                        List.of(tagCreator.createByFullName(name))
                ))).collect(Collectors.toMap(TagView::name, Function.identity()));
    }

    @Override
    public Map<String, Optional<TagView>> findByFullNames(Collection<String> names) {
        Set<String> setNames = Set.copyOf(names);
        return namespace.tags(false)
                .filter(tag -> setNames.contains(tag.fullName()))
                .collect(Collectors.toMap(TagView::fullName, Optional::of));
    }

    private TagNotFoundException tagNotFound(String tagName) {
        return new TagNotFoundException(
                String.format("Tag [%s] not found in namespace [%s]!", tagName, namespace.name())
        );
    }

    private TagView getExactValue(String tagName, List<TagView> values) {
        if (values.isEmpty()) {
            throw tagNotFound(tagName);
        }

        if (values.size() > 2) {
            throw new NonUniqueTagNameException(
                    String.format(
                            "Tag with name [%s] isn't unique in namespace [%s] that has tags %s!",
                            tagName,
                            namespace.name(),
                            values
                    )
            );
        }

        return values.get(0);
    }

    @Override
    public TagView findByNameExact(String name) {
        List<TagView> tags = findByName(name).toList();
        return getExactValue(name, tags);
    }

    @Override
    public TagView findByFullNameExact(String fullName) {
        return findByFullName(fullName).orElseThrow(() -> tagNotFound(fullName));
    }

    @Override
    public Map<String, TagView> findByNamesExact(Collection<String> names) {
        Map<String, List<TagView>> foundTags = findByNames(names);
        return names.stream()
                .map(name -> getExactValue(name, foundTags.getOrDefault(name, List.of())))
                .collect(Collectors.toMap(TagView::name, Function.identity()));
    }

    @Override
    public Map<String, TagView> findByFullNamesExact(Collection<String> names) {
        Set<String> setNames = new HashSet<>(names);
        Map<String, TagView> tags = namespace.tags(false)
                .filter(tag -> setNames.remove(tag.fullName()))
                .collect(Collectors.toMap(TagView::fullName, Function.identity()));

        if (!setNames.isEmpty()) {
            throw new TagNotFoundException(
                    String.format("Tags %s aren't found in namespace [%s]!", setNames, namespace.name())
            );
        }

        return tags;
    }
}
