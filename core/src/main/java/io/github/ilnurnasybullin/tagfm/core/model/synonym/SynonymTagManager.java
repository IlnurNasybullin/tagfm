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

package io.github.ilnurnasybullin.tagfm.core.model.synonym;

import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.util.*;
import java.util.stream.Collectors;

public final class SynonymTagManager implements io.github.ilnurnasybullin.tagfm.core.api.dto.SynonymTagManager {

    private final Map<TreeTag, SynonymClass> synonyms;

    private SynonymTagManager(Map<TreeTag, SynonymClass> synonyms) {
        this.synonyms = synonyms;
    }

    public static SynonymTagManager init() {
        return new SynonymTagManager(new IdentityHashMap<>());
    }

    public static SynonymTagManager of(List<Set<TreeTag>> synonyms) {
        Map<TreeTag, SynonymClass> map = new IdentityHashMap<>();
        synonyms.forEach(tags -> {
            SynonymClass sClass = new SynonymClass();
            tags.forEach(tag -> {
                map.put(tag, sClass);
                sClass.increment();
            });
        });

        return new SynonymTagManager(map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Set<TreeTag>> synonyms() {
        Map<SynonymClass, Set<TreeTag>> map = new IdentityHashMap<>();
        synonyms.forEach((key, value) ->
                map.computeIfAbsent(value, k -> Collections.newSetFromMap(new IdentityHashMap<>())).add(key));

        return new ArrayList<>(map.values());
    }

    private void bind(TreeTag tag, TreeTag synonym) {
        SynonymClass tagClass = synonyms.get(tag);
        SynonymClass synonymClass = synonyms.get(synonym);

        if (tagClass != null && synonymClass != null) {
            unionClasses(tagClass, synonymClass);
            return;
        }

        if (tagClass == null && synonymClass == null) {
            SynonymClass sClass = new SynonymClass();
            put(tag, sClass);
            put(synonym, sClass);
            return;
        }

        SynonymClass notNullClass;
        TreeTag newTag;

        if (tagClass == null) {
            notNullClass = synonymClass;
            newTag = tag;
        } else {
            notNullClass = tagClass;
            newTag = synonym;
        }

        put(newTag, notNullClass);
    }

    @Override
    public void bind(Tag tag, Tag synonym) {
        bind((TreeTag) tag, (TreeTag) synonym);
    }

    private void put(TreeTag tag, SynonymClass synonymClass) {
        synonyms.put(tag, synonymClass);
        synonymClass.increment();
    }

    private void unionClasses(SynonymClass primary, SynonymClass secondary) {
        synonyms.replaceAll((key, value) -> {
            if (value == secondary) {
                primary.increment();
                secondary.decrement();
                return primary;
            }
            return value;
        });
    }

    @Override
    public void unbind(Tag tag) {
        unbind((TreeTag) tag);
    }

    private void unbind(TreeTag tag) {
        SynonymClass synonymClass = synonyms.get(tag);
        if (synonymClass == null) {
            return;
        }

        synonymClass.decrement();

        if (synonymClass.count() < 2) {
            synonyms.entrySet().removeIf(entry -> Objects.equals(entry.getValue(), synonymClass));
        }
    }

    @Override
    public void replace(Tag oldTag, Tag newTag) {
        replace((TreeTag) oldTag, (TreeTag) newTag);
    }

    private void replace(TreeTag oldTag, TreeTag newTag) {
        if (newTag == null) {
            unbind(oldTag);
        }

        SynonymClass primaryClass = synonyms.get(newTag);
        SynonymClass secondaryClass = synonyms.get(oldTag);

        if (primaryClass == null || secondaryClass == null) {
            return;
        }

        unionClasses(primaryClass, secondaryClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TreeTag> synonyms(Tag tag) {
        return synonyms((TreeTag) tag);
    }

    private Set<TreeTag> synonyms(TreeTag tag) {
        if (!synonyms.containsKey(tag)) {
            return Set.of();
        }

        SynonymClass synonymClass = synonyms.get(tag);
        return synonyms.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), synonymClass))
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<TreeTag, Object> synonymMap() {
        return Collections.unmodifiableMap(synonyms);
    }
}
