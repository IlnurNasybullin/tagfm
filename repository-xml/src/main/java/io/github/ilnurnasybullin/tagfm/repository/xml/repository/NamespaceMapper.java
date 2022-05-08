package io.github.ilnurnasybullin.tagfm.repository.xml.repository;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.repository.Namespace;
import io.github.ilnurnasybullin.tagfm.core.repository.Tag;
import io.github.ilnurnasybullin.tagfm.core.repository.TaggedFile;
import io.github.ilnurnasybullin.tagfm.repository.xml.dto.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.repository.xml.entity.FileNamingStrategyEntity;
import io.github.ilnurnasybullin.tagfm.repository.xml.entity.NamespaceEntity;
import io.github.ilnurnasybullin.tagfm.repository.xml.entity.TagEntity;
import io.github.ilnurnasybullin.tagfm.repository.xml.entity.TaggedFileEntity;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NamespaceMapper {

    private final AtomicLong idGenerator;

    public NamespaceMapper() {
        idGenerator = new AtomicLong();
    }

    public NamespaceEntity from(Namespace namespace) {
        NamespaceEntity entity = new NamespaceEntity();
        entity.setCreated(namespace.created());
        entity.setName(namespace.name());

        Map<Tag, TagEntity> tagsMap = namespace.tags()
                .collect(Collectors.toMap(Function.identity(), this::from));

        tagsMap.forEach((tag, tagEntity) -> tag.parent()
                .ifPresent(tagParent -> tagEntity.setParent(tagsMap.get(tagParent))));
        entity.setTags(Set.copyOf(tagsMap.values()));

        FileNamingStrategy strategy = namespace.fileNaming();
        entity.setFileNaming(FileNamingStrategyEntity.from(strategy));

        Path currentPath;
        try {
            currentPath = Path.of(".").toAbsolutePath().toRealPath();
        } catch (IOException e) {
            // this is never thrown
            throw new UncheckedIOException("Problem with reading current directory's url", e);
        }

        Function<Path, String> naming = switch (strategy) {
            case ABSOLUTE -> Path::toString;
            case RELATIVE ->  filePath -> currentPath.resolve(filePath).toString();
        };

        Set<TaggedFileEntity> taggedFiles = namespace.files()
                .stream()
                .map(taggedFile -> from(taggedFile, tagsMap, naming))
                .collect(Collectors.toSet());

        entity.setTaggedFiles(taggedFiles);

        return entity;
    }

    private TaggedFileEntity from(TaggedFile taggedFile, Map<Tag, TagEntity> tagsMap, Function<Path, String> naming) {
        TaggedFileEntity entity = new TaggedFileEntity();

        try {
            entity.setName(naming.apply(taggedFile.file().toAbsolutePath().toRealPath()));
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Invalid file's url [%s]", taggedFile.file()), e);
        }

        Set<TagEntity> tags = taggedFile.<TagEntity>tags()
                .stream()
                .map(tagsMap::get)
                .collect(Collectors.toSet());
        entity.setTags(tags);

        return entity;
    }

    private TagEntity from(Tag tag) {
        TagEntity tagEntity = TagEntity.createWithId(idGenerator.getAndIncrement());
        tagEntity.setName(tag.name());
        return tagEntity;
    }

    public Namespace to(NamespaceEntity entity) {
        return NamespaceDto.singleRoot(entity);
    }
}
