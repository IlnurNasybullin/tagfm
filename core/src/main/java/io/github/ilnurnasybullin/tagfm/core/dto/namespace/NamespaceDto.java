package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.repository.Namespace;
import io.github.ilnurnasybullin.tagfm.core.repository.TreeIterator;

import java.time.ZonedDateTime;
import java.util.*;

public class NamespaceDto implements Namespace {

    private String name;
    private final ZonedDateTime created;
    private FileNamingStrategy fileNaming;

    private final TreeTagDto root;
    private final List<Set<TreeTagDto>> synonyms;
    private final Set<TaggedFileDto> files;

    protected NamespaceDto(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTagDto ROOT,
                           List<Set<TreeTagDto>> synonyms, Set<TaggedFileDto> files) {
        this.name = name;
        this.created = created;
        this.fileNaming = fileNaming;
        this.root = ROOT;
        this.synonyms = synonyms;
        this.files = files;
    }

    @Override
    public String name() {
        return name;
    }

    public void rename(String newName) {
        this.name = newName;
    }

    @Override
    public ZonedDateTime created() {
        return created;
    }

    @Override
    public FileNamingStrategy fileNaming() {
        return fileNaming;
    }

    public void replaceFileNamingStrategy(FileNamingStrategy fileNaming) {
        this.fileNaming = fileNaming;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<TreeTagDto> horizontalTraversal() {
        TreeIterator<TreeTagDto> iterator =
                TreeIterator.horizontalTraversal(
                        root, tag -> tag.children()
                                .values()
                );
        iterator.next();
        return iterator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Set<TreeTagDto>> synonyms() {
        return synonyms;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TaggedFileDto> files() {
        return files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamespaceDto that = (NamespaceDto) o;
        return Objects.equals(name, that.name) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, created);
    }

    @Override
    public String toString() {
        return "NamespaceDto{" +
                "name='" + name + '\'' +
                '}';
    }

    public void addTags(Collection<TreeTagDto> tags) {
        tags.forEach(this::addTag);
    }

    public void addTag(TreeTagDto tag) {
        addTag(tag, root);
    }

    private static void addTag(TreeTagDto tag, TreeTagDto root) {
        if (!root.children().containsKey(tag.name())) {
            tag.reparent(root);
            return;
        }

        TreeTagDto newRoot = root.children().get(tag.name());
        Map.copyOf(tag.children()).forEach((name, child) -> addTag(child, newRoot));
    }
}
