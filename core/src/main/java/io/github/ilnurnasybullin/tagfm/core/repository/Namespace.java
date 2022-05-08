package io.github.ilnurnasybullin.tagfm.core.repository;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Namespace {
    String name();
    ZonedDateTime created();

    FileNamingStrategy fileNaming();

    <T extends TaggedFile> Set<T> files();
    <T extends Tag> Iterator<T> horizontalTraversal();

    <T extends Tag> List<Set<T>> synonyms();

    default <T extends Tag> Stream<T> tags() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                horizontalTraversal(),
                Spliterator.DISTINCT | Spliterator.NONNULL
        ), false);
    }

}
