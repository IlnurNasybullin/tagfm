package io.github.ilnurnasybullin.tagfm.core.repository;

import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymGroupRepoDto;

import java.util.Collection;

public interface SynonymGroupEntity {
    <T extends TagEntity> Collection<T> tags();
}
