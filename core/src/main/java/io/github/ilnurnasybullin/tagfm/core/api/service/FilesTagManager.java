package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.FilesTagManagerService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;

import java.util.Collection;

/**
 * @author Ilnur Nasybullin
 */
public class FilesTagManager implements FilesTagManagerService<TagView> {

    private final NamespaceView namespace;

    private FilesTagManager(NamespaceView namespace) {
        this.namespace = namespace;
    }

    public static FilesTagManager of(NamespaceView namespace) {
        return new FilesTagManager(namespace);
    }

    @Override
    public void replaceTag(TagView oldTag, TagView newTag) {
        if (newTag == null) {
            removeTag(oldTag);
            return;
        }

        namespace.files().stream()
                .map(TaggedFileView::tags)
                .filter(tags -> tags.remove(oldTag))
                .forEach(tags -> tags.add(newTag));
    }

    @Override
    public void removeTag(TagView tag) {
        namespace.files().stream()
                .map(TaggedFileView::tags)
                .forEach(tags -> tags.remove(tag));
    }

    @Override
    public void removeTags(Collection<TagView> tags) {
        namespace.files().stream()
                .map(TaggedFileView::tags)
                .forEach(t -> t.removeAll(tags));
    }
}
