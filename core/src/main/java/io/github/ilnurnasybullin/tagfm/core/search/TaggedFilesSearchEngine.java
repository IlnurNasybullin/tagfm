package io.github.ilnurnasybullin.tagfm.core.search;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;

import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
public interface TaggedFilesSearchEngine extends Predicate<TaggedFileDto> {

}
