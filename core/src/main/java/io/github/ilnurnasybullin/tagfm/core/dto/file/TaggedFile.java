package io.github.ilnurnasybullin.tagfm.core.dto.file;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.nio.file.Path;
import java.util.Set;

public class TaggedFile extends TaggedFileValidator {

    protected TaggedFile(Path file, Set<TreeTagDto> tags) {
        super(file, tags);
    }

    public static TaggedFileDto initWithTags(Path file, Set<TreeTagDto> tags) {
        return new TaggedFile(file, tags);
    }
}
