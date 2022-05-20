package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.Collection;

/**
 * @author Ilnur Nasybullin
 */
public interface FilesTagManagerService<T> {
    void replaceTag(T oldTag, T newTag);
    void removeTag(T tag);
    void removeTags(Collection<T> tags);
}
