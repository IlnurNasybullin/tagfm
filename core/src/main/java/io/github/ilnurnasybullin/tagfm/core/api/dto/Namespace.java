package io.github.ilnurnasybullin.tagfm.core.api.dto;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagIteratorServiceImpl;
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Ilnur Nasybullin
 */
public sealed interface Namespace permits io.github.ilnurnasybullin.tagfm.core.model.namespace.Namespace {
    String name();
    ZonedDateTime created();
    FileNamingStrategy fileNaming();
    void replaceFileNamingStrategy(FileNamingStrategy fileNaming);

    Tag root();
    <TF extends TaggedFile> Set<TF> files();
    SynonymTagManager synonymsManager();

    default Stream<Tag> tags(boolean withRoot) {
        Iterator<Tag> iterator = TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE
                .iterator(root(), tag -> tag.children().values());
        if (!withRoot) {
            iterator.next();
        }

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,
                    Spliterator.DISTINCT | Spliterator.NONNULL
                ), false);
    }

    default <T extends Tag> List<Set<T>> synonyms() {
        return synonymsManager().synonyms();
    }
}
