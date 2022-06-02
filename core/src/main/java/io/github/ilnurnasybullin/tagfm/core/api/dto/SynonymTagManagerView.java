package io.github.ilnurnasybullin.tagfm.core.api.dto;

import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymTagManager;

import java.util.*;

/**
 * @author Ilnur Nasybullin
 */
public sealed interface SynonymTagManagerView permits SynonymTagManager {
    <S extends SynonymGroupView> List<S> synonymGroups();

    <T extends TagView> void bind(T tag, T synonym);
    <T extends TagView> void unbind(T tag);
    <T extends TagView> void replace(T oldTag, T newTag);

    default <T extends TagView> void bind(Collection<T> tags) {
        if (tags.isEmpty() || tags.size() < 2) {
            return;
        }

        Iterator<T> iterator = tags.iterator();
        T first = iterator.next();
        while (iterator.hasNext()) {
            bind(first, iterator.next());
        }
    }

    <T extends TagView> Collection<T> synonyms(T tag);
    <T extends TagView, S extends SynonymGroupView> Map<T, S> synonymMap();
}
