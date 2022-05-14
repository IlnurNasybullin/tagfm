package io.github.ilnurnasybullin.tagfm.repository.xml.dto;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.repository.Namespace;
import io.github.ilnurnasybullin.tagfm.core.iterator.TreeIterator;
import io.github.ilnurnasybullin.tagfm.repository.xml.entity.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NamespaceDto implements Namespace {

    private final String name;
    private final ZonedDateTime created;
    private final FileNamingStrategy fileNaming;
    private final Set<TaggedFileEntity> taggedFiles;
    private final TagEntity root;
    private final List<Set<TagEntity>> synonyms;

    private NamespaceDto(String name, ZonedDateTime created, FileNamingStrategy fileNaming,
                         Set<TaggedFileEntity> taggedFiles, TagEntity root,
                         List<Set<TagEntity>> synonyms) {
        this.name = name;
        this.created = created;
        this.fileNaming = fileNaming;
        this.taggedFiles = taggedFiles;
        this.root = root;
        this.synonyms = synonyms;
    }

    public static NamespaceDto singleRoot(NamespaceEntity namespace) {
        FileNamingStrategy fileNaming = FileNamingStrategyEntity.to(namespace.getFileNaming());

        TagEntity root = createRoot(namespace.getTags());
        renameFiles(namespace);

        List<Set<TagEntity>> synonyms = namespace.getSynonyms()
                .stream()
                .map(SynonymsEntity::getTags)
                .toList();

        return new NamespaceDto(
                namespace.getName(), namespace.created(), fileNaming,
                namespace.getTaggedFiles(), root, synonyms);
    }

    private static TagEntity createRoot(Set<TagEntity> tags) {
        TagEntity root = new TagEntity();
        tags.stream()
                .filter(tag -> tag.getParent() == null)
                .forEach(tag -> root.children().add(tag));

        return root;
    }

    private static void renameFiles(NamespaceEntity namespace) {
        for (TaggedFileEntity taggedFile: namespace.getTaggedFiles()) {
            try {
                taggedFile.setName(Paths.get(taggedFile.getName()).toRealPath().toString());
            } catch (IOException e) {
                throw new UncheckedIOException(
                        String.format("Exception with reading file [%s] URL", taggedFile.getName()), e
                );
            }
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ZonedDateTime created() {
        return created;
    }

    @Override
    public FileNamingStrategy fileNaming() {
        return fileNaming;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TaggedFileEntity> files() {
        return taggedFiles;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<TagEntity> horizontalTraversal() {
        Iterator<TagEntity> iterator = TreeIterator.horizontalTraversal(root, TagEntity::children);
        iterator.next();
        return iterator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Set<TagEntity>> synonyms() {
        return synonyms;
    }
}
