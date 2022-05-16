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

package io.github.ilnurnasybullin.tagfm.core.dto.file;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class TaggedFileManager {

    private final Set<TaggedFileDto> files;

    private TaggedFileManager(Set<TaggedFileDto> files) {
        this.files = files;
    }

    public static TaggedFileManager of() {
        return of(Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    public static TaggedFileManager of(Set<TaggedFileDto> files) {
        return new TaggedFileManager(files);
    }

    public Set<TaggedFileDto> files() {
        return files;
    }

    public void replaceTag(TreeTagDto oldTag, TreeTagDto newTag) {
        if (newTag == null) {
            removeTag(oldTag);
            return;
        }

        files.stream()
                .map(TaggedFileDto::tags)
                .filter(tags -> tags.remove(oldTag))
                .forEach(tags -> tags.add(newTag));
    }

    public void removeTag(TreeTagDto tag) {
        files.stream()
                .map(TaggedFileDto::tags)
                .forEach(tags -> tags.remove(tag));
    }

    public void removeTags(Collection<TreeTagDto> tags) {
        files.stream()
                .map(TaggedFileDto::tags)
                .forEach(t -> t.removeAll(tags));
    }

}
