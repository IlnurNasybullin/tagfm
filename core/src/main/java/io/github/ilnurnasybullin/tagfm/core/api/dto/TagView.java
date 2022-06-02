package io.github.ilnurnasybullin.tagfm.core.api.dto;

import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.util.Map;
import java.util.Optional;

/**
 * @author Ilnur Nasybullin
 */
public sealed interface TagView permits TreeTag {
    void rename(String newName);
    String name();
    String fullName();
    <T extends TagView> Optional<T> parent();
    <T extends TagView> Map<String, T> children();
}
