package io.github.ilnurnasybullin.tagfm.core.model.tag;

import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.repository.TagRepoDto;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public final class TreeTagRepoDto implements TagRepoDto {

    private final Tag innerTag;

    private TreeTagRepoDto(Tag innerTag) {
        this.innerTag = innerTag;
    }

    public static TreeTagRepoDto of(Tag tag) {
        return new TreeTagRepoDto(tag);
    }

    @Override
    public String name() {
        return innerTag.name();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TreeTagRepoDto> children() {
        return innerTag.children().values().stream()
                .map(TreeTagRepoDto::of)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<TreeTagRepoDto> parent() {
        return innerTag.parent().map(TreeTagRepoDto::of);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeTagRepoDto that = (TreeTagRepoDto) o;
        return Objects.equals(innerTag, that.innerTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerTag);
    }
}
