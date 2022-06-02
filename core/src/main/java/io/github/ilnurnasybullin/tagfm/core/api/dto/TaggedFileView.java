package io.github.ilnurnasybullin.tagfm.core.api.dto;

import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFile;

import java.nio.file.Path;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
public sealed interface TaggedFileView permits TaggedFile {
    Path file();
    <T extends TagView> Set<T> tags();
}
