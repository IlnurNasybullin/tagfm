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
