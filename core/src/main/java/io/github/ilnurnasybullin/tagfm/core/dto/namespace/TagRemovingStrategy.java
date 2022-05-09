package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

public enum TagRemovingStrategy {
    REMOVE_CHILDREN,
    UP_AND_MERGE_CHILDREN,
    UP_AND_REBASE_OLD,
    UP_AND_REBASE_NEW,
    UP_CHILDREN_WITHOUT_CONFLICTS
}
