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
import io.github.ilnurnasybullin.tagfm.core.api.service.IllegalTagForReplacing;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagGroupNotFoundException;
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
            mergeGroups(tagGroup, synonymGroup);
            return;
        }

        if (tagGroup == null && synonymGroup == null) {
            SynonymGroup sClass = new SynonymGroup();
            putInGroup(sClass, tag);
            putInGroup(sClass, synonym);
            return;
        }

        if (tagGroup == null) {
            putInGroup(synonymGroup, tag);
        } else {
            putInGroup(tagGroup, synonym);
        }
    }

    @Override
    public void bind(TagView tag, TagView synonym) {
        bind((TreeTag) tag, (TreeTag) synonym);
    }

    private void putInGroup(SynonymGroup group, TreeTag tag) {
        synonyms.put(tag, group);
        group.tags().add(tag);
    }

    private void mergeGroups(SynonymGroup group1, SynonymGroup group2) {
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

        unbindInGroup(synonymGroup, tag);
    }

    private void unbindInGroup(SynonymGroup group, TreeTag tag) {
        group.tags().remove(tag);
        if (group.size() < 2) {
            group.tags().forEach(synonyms::remove);
        }
    }

    private boolean merge(TreeTag tag, TreeTag synonym) {
        SynonymGroup tagGroup = synonyms.get(tag);
        SynonymGroup synonymGroup = synonyms.get(synonym);

        if (tagGroup == null || synonymGroup == null) {
            return false;
        }

        mergeGroups(tagGroup, synonymGroup);
        return true;
    }

    @Override
    public boolean merge(TagView tag, TagView synonym) {
        return merge((TreeTag) tag, (TreeTag) synonym);
    }

    private void replace(TreeTag oldTag, TreeTag newTag) {
        SynonymGroup oldTagGroup = synonyms.get(oldTag);
        SynonymGroup newTagGroup = synonyms.get(newTag);

        if (oldTagGroup == null) {
            throw new TagGroupNotFoundException(
                    String.format("Not found synonyms group for tag with name [%s]", oldTag)
            );
        }

        if (newTagGroup != null) {
            throw new IllegalTagForReplacing(
                    String.format(
                            "Tag with name [%s] can't be replaced - it has already bound with tags %s",
                            newTag, newTagGroup.tags()
                    )
            );
        }

        replaceInGroup(oldTagGroup, oldTag, newTag);
    }

    private void replaceInGroup(SynonymGroup group, TreeTag oldTag, TreeTag newTag) {
        unbindInGroup(group, oldTag);
        putInGroup(group, newTag);
    }

    @Override
    public void replace(TagView oldTag, TagView newTag) {
        replace((TreeTag) oldTag, (TreeTag) newTag);
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
