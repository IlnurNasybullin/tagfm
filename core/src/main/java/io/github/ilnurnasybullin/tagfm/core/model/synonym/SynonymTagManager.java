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

import io.github.ilnurnasybullin.tagfm.core.api.dto.SynonymTagManagerView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.util.*;

public final class SynonymTagManager implements SynonymTagManagerView {

    private final Map<TreeTag, SynonymGroup> synonyms;

    private SynonymTagManager(Map<TreeTag, SynonymGroup> synonyms) {
        this.synonyms = synonyms;
    }

    public static SynonymTagManager init() {
        return new SynonymTagManager(new IdentityHashMap<>());
    }

    public static SynonymTagManager of(List<SynonymGroup> synonyms) {
        Map<TreeTag, SynonymGroup> map = new IdentityHashMap<>();
        synonyms.forEach(group -> group.tags().forEach(tag -> map.put(tag, group)));

        return new SynonymTagManager(map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SynonymGroup> synonymGroups() {
        return List.copyOf(new HashSet<>(synonyms.values()));
    }

    private void bind(TreeTag tag, TreeTag synonym) {
        SynonymGroup tagGroup = synonyms.get(tag);
        SynonymGroup synonymGroup = synonyms.get(synonym);

        if (tagGroup != null && synonymGroup != null) {
            unionClasses(tagGroup, synonymGroup);
            return;
        }

        if (tagGroup == null && synonymGroup == null) {
            SynonymGroup sClass = new SynonymGroup();
            put(tag, sClass);
            put(synonym, sClass);
            return;
        }

        SynonymGroup notNullClass;
        TreeTag newTag;

        if (tagGroup == null) {
            notNullClass = synonymGroup;
            newTag = tag;
        } else {
            notNullClass = tagGroup;
            newTag = synonym;
        }

        put(newTag, notNullClass);
    }

    @Override
    public void bind(TagView tag, TagView synonym) {
        bind((TreeTag) tag, (TreeTag) synonym);
    }

    private void put(TreeTag tag, SynonymGroup synonymGroup) {
        synonyms.put(tag, synonymGroup);
        synonymGroup.tags().add(tag);
    }

    private void unionClasses(SynonymGroup group1, SynonymGroup group2) {
        if (Objects.equals(group1, group2)) {
            return;
        }

        boolean group1IsPrimary = group1.tags().size() > group2.tags().size();

        SynonymGroup primary = group1IsPrimary ? group1 : group2;
        SynonymGroup secondary = group1IsPrimary ? group2 : group1;

        primary.tags().addAll(secondary.tags());
        secondary.tags().forEach(tag -> synonyms.replace(tag, primary));
    }

    @Override
    public void unbind(TagView tag) {
        unbind((TreeTag) tag);
    }

    private void unbind(TreeTag tag) {
        SynonymGroup synonymGroup = synonyms.get(tag);
        if (synonymGroup == null) {
            return;
        }

        synonymGroup.tags().remove(tag);
        if (synonymGroup.size() < 2) {
            synonymGroup.tags().forEach(synonyms::remove);
        }
    }

    @Override
    public void replace(TagView oldTag, TagView newTag) {
        replace((TreeTag) oldTag, (TreeTag) newTag);
    }

    private void replace(TreeTag oldTag, TreeTag newTag) {
        if (newTag == null) {
            unbind(oldTag);
        }

        SynonymGroup primaryClass = synonyms.get(newTag);
        SynonymGroup secondaryClass = synonyms.get(oldTag);

        if (primaryClass == null || secondaryClass == null) {
            return;
        }

        unionClasses(primaryClass, secondaryClass);
    }

    @Override
    public Set<TagView> synonyms(TagView tag) {
        return synonyms((TreeTag) tag);
    }

    private Set<TagView> synonyms(TreeTag tag) {
        return Set.copyOf(synonyms.getOrDefault(tag, new SynonymGroup()).tags());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<TreeTag, SynonymGroup> synonymMap() {
        return Collections.unmodifiableMap(synonyms);
    }
}
