package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileManager;
import io.github.ilnurnasybullin.tagfm.core.dto.synonym.SynonymTagManager;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.core.iterator.TreeIteratorsFactory;
import io.github.ilnurnasybullin.tagfm.core.repository.Namespace;
import io.github.ilnurnasybullin.tagfm.core.iterator.TreeIterator;

import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class NamespaceDto implements Namespace {

    private String name;
    private final ZonedDateTime created;
    private FileNamingStrategy fileNaming;

    private final TreeTagDto root;

    private final SynonymTagManager synonymsManager;
    private final TaggedFileManager fileManager;

    protected NamespaceDto(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTagDto ROOT,
                           SynonymTagManager synonymsManager, TaggedFileManager fileManager) {
        this.name = name;
        this.created = created;
        this.fileNaming = fileNaming;
        this.root = ROOT;
        this.synonymsManager = synonymsManager;
        this.fileManager = fileManager;
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
        Iterator<TreeTagDto> iterator =
                TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE.iterator(
                        root, tag -> tag.children()
                                .values()
                );
        iterator.next();
        return iterator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Set<TreeTagDto>> synonyms() {
        return synonymsManager.synonyms();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TaggedFileDto> files() {
        return fileManager.files();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stream<TreeTagDto> tags() {
        return Namespace.super.tags();
    }

    TreeTagDto root() {
        return root;
    }

    public TaggedFileManager fileManager() {
        return fileManager;
    }

    public SynonymTagManager synonymsManager() {
        return synonymsManager;
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
