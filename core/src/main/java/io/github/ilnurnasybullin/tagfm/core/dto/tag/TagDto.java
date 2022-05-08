package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import io.github.ilnurnasybullin.tagfm.core.repository.Tag;

import java.util.Map;

public interface TagDto extends Tag {
    TreeTagDto rename(String newName);
    String fullName();
    TreeTagDto reparent(TreeTagDto parent);
    Map<String, TreeTagDto> children();
}
