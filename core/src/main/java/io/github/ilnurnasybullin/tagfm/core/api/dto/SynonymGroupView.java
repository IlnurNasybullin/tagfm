package io.github.ilnurnasybullin.tagfm.core.api.dto;

import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymGroup;

import java.util.Collection;

public sealed interface SynonymGroupView permits SynonymGroup {
    <T extends TagView> Collection<T> tags();

    default int size() {
        return tags().size();
    }
}
