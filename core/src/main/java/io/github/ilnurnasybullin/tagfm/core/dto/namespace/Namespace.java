package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.core.repository.Tag;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Namespace extends NamespaceValidator {

    protected Namespace(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTagDto ROOT,
                        List<Set<TreeTagDto>> synonyms, Set<TaggedFileDto> files) {
        super(name, created, fileNaming, ROOT, synonyms, files);
    }

    static NamespaceDto from(io.github.ilnurnasybullin.tagfm.core.repository.Namespace namespace) {
        TreeTagDto ROOT = TreeTag.root();
        Map<Tag, TreeTagDto> tagsMap = new HashMap<>();
        namespace.tags()
                .forEach(tag -> {
                    TreeTagDto parent = tag.parent()
                            .map(tagsMap::get)
                            .orElse(ROOT);
                    TreeTagDto tagDto = TreeTag.initWithParent(tag.name(), parent);
                    tagsMap.put(tag, tagDto);
                });

        List<Set<TreeTagDto>> synonyms = namespace.synonyms()
                .stream()
                .map(tags -> tags.stream()
                        .map(tagsMap::get)
                        .collect(
                                () -> Collections.<TreeTagDto>newSetFromMap(new IdentityHashMap<>()),
                                Set::add,
                                Set::addAll
                        ))
                .collect(Collectors.toList());

        Set<TaggedFileDto> files = namespace.files().stream().map(file -> {
            Set<TreeTagDto> tags = file.<Tag>tags()
                    .stream()
                    .map(tagsMap::get)
                    .collect(
                            () -> Collections.newSetFromMap(new IdentityHashMap<>()),
                            Set::add,
                            Set::addAll
                    );
            return TaggedFile.initWithTags(file.file(), tags);
        }).collect(Collectors.toSet());

        return new Namespace(namespace.name(), namespace.created(), namespace.fileNaming(), ROOT, synonyms, files);
    }

    static NamespaceDto init(String name, FileNamingStrategy fileNaming) {
        ZonedDateTime now = LocalDateTime.now(Clock.systemUTC())
                .atZone(ZoneId.of("UTC"));

        return new Namespace(name, now, fileNaming, TreeTag.root(), new ArrayList<>(), new HashSet<>());
    }
}
