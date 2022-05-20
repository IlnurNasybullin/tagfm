package io.github.ilnurnasybullin.tagfm.core.api.dto;

import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.util.*;

/**
 * @author Ilnur Nasybullin
 */
public sealed interface SynonymTagManager permits io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymTagManager {
    <T extends Tag> List<Set<T>> synonyms();
    <T extends Tag> void bind(T tag, T synonym);

    <T extends Tag> void unbind(T tag);

    <T extends Tag> void replace(T oldTag, T newTag);

    default <T extends Tag> void bind(Collection<T> tags) {
        if (tags.isEmpty() || tags.size() < 2) {
            return;
        }

        Iterator<T> iterator = tags.iterator();
        T first = iterator.next();
        while (iterator.hasNext()) {
            bind(first, iterator.next());
        }
    }

    <T extends Tag >Set<T> synonyms(T tag);
    <T extends Tag> Map<T, Object> synonymMap();
}
