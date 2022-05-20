package io.github.ilnurnasybullin.tagfm.core.model.file;

import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
public sealed class TaggedFile implements io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile permits TaggedFileSafety {

    private Path file;
    private final Set<TreeTag> tags;

    protected TaggedFile(Path file, Set<TreeTag> tags) {
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
    public Set<TreeTag> tags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaggedFile that = (TaggedFile) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    @Override
    public String toString() {
        return String.format("TaggedFile{file=[%s]}", file);
    }
    
}
