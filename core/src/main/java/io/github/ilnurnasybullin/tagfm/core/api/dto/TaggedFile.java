package io.github.ilnurnasybullin.tagfm.core.api.dto;

import java.nio.file.Path;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
public sealed interface TaggedFile permits io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFile {
    Path file();
    <T extends Tag> Set<T> tags();
}
