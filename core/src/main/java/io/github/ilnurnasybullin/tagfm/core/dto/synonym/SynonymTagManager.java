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

package io.github.ilnurnasybullin.tagfm.core.dto.synonym;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.*;
import java.util.stream.Collectors;

public class SynonymTagManager {

    private final Map<TreeTagDto, SynonymClass> synonyms;

    private SynonymTagManager(Map<TreeTagDto, SynonymClass> synonyms) {
        this.synonyms = synonyms;
    }

    public static SynonymTagManager of() {
        return new SynonymTagManager(new IdentityHashMap<>());
    }

    public static SynonymTagManager of(List<Set<TreeTagDto>> synonyms) {
        Map<TreeTagDto, SynonymClass> map = new IdentityHashMap<>();
        synonyms.forEach(tags -> {
            SynonymClass sClass = new SynonymClass();
            tags.forEach(tag -> {
                map.put(tag, sClass);
                sClass.increment();
            });
        });

        return new SynonymTagManager(map);
    }

    public List<Set<TreeTagDto>> synonyms() {
        Map<SynonymClass, Set<TreeTagDto>> map = new IdentityHashMap<>();
        synonyms.forEach((key, value) ->
                map.computeIfAbsent(value, k -> Collections.newSetFromMap(new IdentityHashMap<>())).add(key));

        return new ArrayList<>(map.values());
    }

    public void bind(TreeTagDto tag, TreeTagDto synonym) {
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
        TreeTagDto newTag;

        if (tagClass == null) {
            notNullClass = synonymClass;
            newTag = tag;
        } else {
            notNullClass = tagClass;
            newTag = synonym;
        }

        put(newTag, notNullClass);
    }

    private void put(TreeTagDto tag, SynonymClass synonymClass) {
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

    public void unbind(TreeTagDto tag) {
        SynonymClass synonymClass = synonyms.get(tag);
        if (synonymClass == null) {
            return;
        }

        synonymClass.decrement();

        if (synonymClass.count() < 2) {
            synonyms.entrySet().removeIf(entry -> Objects.equals(entry.getValue(), synonymClass));
        }
    }
    
    public void replace(TreeTagDto oldTag, TreeTagDto newTag) {
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

    public void bind(Collection<TreeTagDto> tags) {
        if (tags.isEmpty() || tags.size() < 2) {
            return;
        }

        Iterator<TreeTagDto> iterator = tags.iterator();
        TreeTagDto first = iterator.next();
        while (iterator.hasNext()) {
            bind(first, iterator.next());
        }
    }

    public Set<TreeTagDto> synonyms(TreeTagDto tag) {
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

    public Map<TreeTagDto, Object> synonymMap() {
        return Collections.unmodifiableMap(synonyms);
    }
}
