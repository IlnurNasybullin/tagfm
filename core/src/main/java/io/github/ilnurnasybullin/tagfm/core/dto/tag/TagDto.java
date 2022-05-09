package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import io.github.ilnurnasybullin.tagfm.core.repository.Tag;

import java.util.Map;

public interface TagDto extends Tag {
    void rename(String newName);
    String fullName();
    void reparent(TreeTagDto parent);
    Map<String, TreeTagDto> children();
}
