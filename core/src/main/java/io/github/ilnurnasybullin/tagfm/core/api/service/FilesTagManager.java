package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.FilesTagManagerService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;

import java.util.Collection;

/**
 * @author Ilnur Nasybullin
 */
public class FilesTagManager implements FilesTagManagerService<Tag> {

    private final Namespace namespace;

    private FilesTagManager(Namespace namespace) {
        this.namespace = namespace;
    }

    public static FilesTagManager of(Namespace namespace) {
        return new FilesTagManager(namespace);
    }

    @Override
    public void replaceTag(Tag oldTag, Tag newTag) {
        if (newTag == null) {
            removeTag(oldTag);
            return;
        }

        namespace.files().stream()
                .map(TaggedFile::tags)
                .filter(tags -> tags.remove(oldTag))
                .forEach(tags -> tags.add(newTag));
    }

    @Override
    public void removeTag(Tag tag) {
        namespace.files().stream()
                .map(TaggedFile::tags)
                .forEach(tags -> tags.remove(tag));
    }

    @Override
    public void removeTags(Collection<Tag> tags) {
        namespace.files().stream()
                .map(TaggedFile::tags)
                .forEach(t -> t.removeAll(tags));
    }
}
