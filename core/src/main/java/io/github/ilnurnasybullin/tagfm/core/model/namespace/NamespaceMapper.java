package io.github.ilnurnasybullin.tagfm.core.model.namespace;

import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFileSafety;
import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymGroup;
import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymTagManager;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTagSafety;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceEntity;
import io.github.ilnurnasybullin.tagfm.core.repository.TagEntity;
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public class NamespaceMapper {

    private final NamespaceEntity namespace;

    private NamespaceMapper(NamespaceEntity namespace) {
        this.namespace = namespace;
    }

    public static NamespaceMapper of(NamespaceEntity namespace) {
        return new NamespaceMapper(namespace);
    }

    public Namespace mapping() {
        TreeTag root = TreeTagSafety.root();
        Map<TagEntity, TreeTag> tagsMap = new HashMap<>();
        Iterator<TagEntity> iterator = TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE
                        .iterator(namespace.root(), TagEntity::children);

        iterator.next();
        iterator.forEachRemaining(tagRep -> {
            TreeTag parent = tagRep.parent()
                    .map(tagsMap::get)
                    .orElse(root);

            TreeTag tag = TreeTagSafety.initWithParent(tagRep.name(), parent);
            tagsMap.put(tagRep, tag);
        });

        List<SynonymGroup> synonyms = namespace.synonymGroups()
                .stream()
                .map(sGroup -> {
                    List<TreeTag> tags = sGroup.tags()
                            .stream()
                            .map(tagsMap::get)
                            .collect(Collectors.toList());
                    return new SynonymGroup(tags);
                })
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
