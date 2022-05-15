package io.github.ilnurnasybullin.tagfm.core.search;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;

import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
interface TaggedFilesFilter extends Predicate<TaggedFileDto> { }
