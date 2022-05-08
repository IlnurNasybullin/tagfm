package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.repository.Tag;
import io.github.ilnurnasybullin.tagfm.core.service.Publisher;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Namespace extends NamespaceValidator {

    private final Publisher publisher;
    private final BiConsumer<Object, Object> updateTag;
    private final BiConsumer<Object, Object> updateFile;

    protected Namespace(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTagDto ROOT,
                        List<Set<TreeTagDto>> synonyms, Set<TaggedFileDto> files) {
        super(name, created, fileNaming, ROOT, synonyms, files);
        publisher = Publisher.get();

        updateTag = this::updateTag;
        publisher.addSubscriber(TreeTag.FULL_NAME_PROPERTY, updateTag);
        publisher.addSubscriber(TreeTag.PARENT_TAG_PROPERTY, updateTag);

        updateFile = this::updateFile;
        publisher.addSubscriber(TaggedFile.FILE_PROPERTY, updateFile);
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
                        .collect(Collectors.toSet()))
                .collect(Collectors.toList());

        Set<TaggedFileDto> files = namespace.files().stream().map(file -> {
            Set<TreeTagDto> tags = file.<Tag>tags()
                    .stream()
                    .map(tagsMap::get)
                    .collect(Collectors.toSet());
            return TaggedFile.initWithTags(file.file(), tags);
        }).collect(Collectors.toSet());

        return new Namespace(namespace.name(), namespace.created(), namespace.fileNaming(), ROOT, synonyms, files);
    }

    static NamespaceDto init(String name, FileNamingStrategy fileNaming) {
        ZonedDateTime now = LocalDateTime.now(Clock.systemUTC())
                .atZone(ZoneId.of("UTC"));

        return new Namespace(name, now, fileNaming, TreeTag.root(), new ArrayList<>(), new HashSet<>());
    }

    @Override
    public NamespaceDto rename(String newName) {
        NamespaceDto newNamespace = super.rename(newName);
        publisher.removeSubscriber(TreeTag.FULL_NAME_PROPERTY, updateTag);
        publisher.removeSubscriber(TreeTag.PARENT_TAG_PROPERTY, updateTag);
        publisher.removeSubscriber(TaggedFile.FILE_PROPERTY, updateFile);
        return newNamespace;
    }

    private void updateFile(Object oldValue, Object newValue) {
        TaggedFileDto oldFile = (TaggedFileDto) oldValue;
        TaggedFileDto newFile = (TaggedFileDto) newValue;

        if (files().remove(oldFile)) {
            files().add(newFile);
        }
    }

    private void updateTag(Object oldValue, Object newValue) {
        TreeTagDto oldTag = (TreeTagDto) oldValue;
        TreeTagDto newTag=  (TreeTagDto) newValue;

        synonyms().stream()
                .filter(tags -> tags.remove(oldTag))
                .forEach(tags -> tags.add(newTag));
    }
}
