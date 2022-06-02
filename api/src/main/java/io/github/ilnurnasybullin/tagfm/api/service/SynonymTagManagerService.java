package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.Collection;
import java.util.Iterator;

public interface SynonymTagManagerService<T> {

    void bind(T tag, T synonym);
    void unbind(T tag);
    void replace(T oldTag, T newTag);

    Collection<T> synonyms(T tag);

    default void bind(Collection<T> tags) {
        if (tags.isEmpty() || tags.size() < 2) {
            return;
        }

        Iterator<T> iterator = tags.iterator();
        T first = iterator.next();
        while (iterator.hasNext()) {
            bind(first, iterator.next());
        }
    }
}
