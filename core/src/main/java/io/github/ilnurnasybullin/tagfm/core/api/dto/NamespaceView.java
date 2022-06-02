package io.github.ilnurnasybullin.tagfm.core.api.dto;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.model.namespace.Namespace;
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Ilnur Nasybullin
 */
public sealed interface NamespaceView permits Namespace {
    String name();
    ZonedDateTime created();
    FileNamingStrategy fileNaming();
    void replaceFileNamingStrategy(FileNamingStrategy fileNaming);

    TagView root();
    <TF extends TaggedFileView> Set<TF> files();
    SynonymTagManagerView synonymsManager();

    default Stream<TagView> tags(boolean withRoot) {
        Iterator<TagView> iterator = TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE
                .iterator(root(), tag -> tag.children().values());
        if (!withRoot) {
            iterator.next();
        }

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,
                    Spliterator.DISTINCT | Spliterator.NONNULL
                ), false);
    }

    default List<SynonymGroupView> synonymGroups() {
        return synonymsManager().synonymGroups();
    }
}
