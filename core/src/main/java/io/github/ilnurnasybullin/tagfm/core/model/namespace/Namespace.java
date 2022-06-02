package io.github.ilnurnasybullin.tagfm.core.model.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymTagManager;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
public sealed class Namespace implements NamespaceView permits NamespaceSafety {

    private String name;
    private final ZonedDateTime created;
    private FileNamingStrategy fileNaming;
    private final TreeTag root;
    private final SynonymTagManager synonymsManager;
    private final Set<TaggedFile> files;

    protected Namespace(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTag root,
                        SynonymTagManager synonymsManager, Set<TaggedFile> files) {
        this.name = name;
        this.created = created;
        this.fileNaming = fileNaming;
        this.root = root;
        this.synonymsManager = synonymsManager;
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

    @Override
    public void replaceFileNamingStrategy(FileNamingStrategy fileNaming) {
        this.fileNaming = fileNaming;
    }

    @SuppressWarnings("unchecked")
    public Set<TaggedFile> files() {
        return files;
    }

    public TreeTag root() {
        return root;
    }

    @Override
    public SynonymTagManager synonymsManager() {
        return synonymsManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Namespace namespace = (Namespace) o;
        return Objects.equals(name, namespace.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
