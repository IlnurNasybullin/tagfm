package io.github.ilnurnasybullin.tagfm.core.dto.file;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.core.repository.TaggedFile;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

public class TaggedFileDto implements TaggedFile {

    private Path file;
    private final Set<TreeTagDto> tags;

    protected TaggedFileDto(Path file, Set<TreeTagDto> tags) {
        this.file = file;
        this.tags = tags;
    }

    @Override
    public Path file() {
        return file;
    }

    public void replace(Path file) {
        this.file = file;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TreeTagDto> tags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaggedFileDto that = (TaggedFileDto) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    @Override
    public String toString() {
        return String.format("TaggedFileDto{file=[%s]}", file);
    }
}
