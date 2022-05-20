package io.github.ilnurnasybullin.tagfm.core.model.namespace;

import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFileSafety;
import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymTagManager;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTagSafety;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepoDto;
import io.github.ilnurnasybullin.tagfm.core.repository.TagRepoDto;
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public class NamespaceMapper {

    private final NamespaceRepoDto namespace;

    private NamespaceMapper(NamespaceRepoDto namespace) {
        this.namespace = namespace;
    }

    public static NamespaceMapper of(NamespaceRepoDto namespace) {
        return new NamespaceMapper(namespace);
    }

    public Namespace mapping() {
        TreeTag root = TreeTagSafety.root();
        Map<TagRepoDto, TreeTag> tagsMap = new HashMap<>();
        Iterator<TagRepoDto> iterator = TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE
                        .iterator(namespace.root(), TagRepoDto::children);

        iterator.next();
        iterator.forEachRemaining(tagRep -> {
            TreeTag parent = tagRep.parent()
                    .map(tagsMap::get)
                    .orElse(root);

            TreeTag tag = TreeTagSafety.initWithParent(tagRep.name(), parent);
            tagsMap.put(tagRep, tag);
        });

        List<Set<TreeTag>> synonyms = namespace.synonyms()
                .stream()
                .map(tags -> tags.stream()
                        .map(tagsMap::get)
                        .collect(
                                () -> Collections.<TreeTag>newSetFromMap(new IdentityHashMap<>()),
                                Set::add,
                                Set::addAll
                        ))
                .collect(Collectors.toList());

        Set<TaggedFile> files = namespace.files().stream().map(file -> {
            Set<TreeTag> tags = file.tags()
                    .stream()
                    .map(tagsMap::get)
                    .collect(
                            () -> Collections.newSetFromMap(new IdentityHashMap<>()),
                            Set::add,
                            Set::addAll
                    );
            return TaggedFileSafety.initWithTags(file.file(), tags);
        }).collect(Collectors.toSet());

        return NamespaceSafety.of(namespace.name(), namespace.created(), namespace.fileNaming(), root,
                SynonymTagManager.of(synonyms), files);
    }

}
