package io.github.ilnurnasybullin.tagfm.core.model.namespace;

import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public class NamespaceMapper implements Function<NamespaceEntity, NamespaceView> {

    @Override
    public NamespaceView apply(NamespaceEntity namespace) {
        return mapping(namespace);
    }

    public NamespaceView mapping(NamespaceEntity namespace) {
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
