package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.repository.Namespace;
import io.github.ilnurnasybullin.tagfm.core.repository.TreeIterator;

import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NamespaceDto implements Namespace {

    private final String name;
    private final ZonedDateTime created;
    private final FileNamingStrategy fileNaming;

    private final TreeTagDto ROOT;
    private final List<Set<TreeTagDto>> synonyms;
    private final Set<TaggedFileDto> files;

    protected NamespaceDto(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTagDto ROOT,
                           List<Set<TreeTagDto>> synonyms, Set<TaggedFileDto> files) {
        this.name = name;
        this.created = created;
        this.fileNaming = fileNaming;
        this.ROOT = ROOT;
        this.synonyms = synonyms;
        this.files = files;
    }

    @Override
    public String name() {
        return name;
    }

    public NamespaceDto rename(String newName) {
        return new NamespaceDto(newName, created, fileNaming, ROOT, synonyms, files);
    }

    @Override
    public ZonedDateTime created() {
        return created;
    }

    @Override
    public FileNamingStrategy fileNaming() {
        return fileNaming;
    }

    public NamespaceDto replaceFileNamingStrategy(FileNamingStrategy fileNaming) {
        return new NamespaceDto(name, created, fileNaming, ROOT, synonyms, files);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<TreeTagDto> horizontalTraversal() {
        TreeIterator<TreeTagDto> iterator =
                TreeIterator.horizontalTraversal(
                        ROOT, tag -> tag.children()
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
}
