package io.github.ilnurnasybullin.tagfm.core.model.file;

import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTagRepoDto;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public final class TaggedFileRepoDto implements io.github.ilnurnasybullin.tagfm.core.repository.TaggedFileRepoDto {

    private final TaggedFile innerFile;

    private TaggedFileRepoDto(TaggedFile innerFile) {
        this.innerFile = innerFile;
    }

    public static TaggedFileRepoDto of(TaggedFile innerFile) {
        return new TaggedFileRepoDto(innerFile);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TreeTagRepoDto> tags() {
        return innerFile.tags()
                .stream()
                .map(TreeTagRepoDto::of)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Path file() {
        return innerFile.file();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaggedFileRepoDto that = (TaggedFileRepoDto) o;
        return Objects.equals(innerFile, that.innerFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerFile);
    }
}
