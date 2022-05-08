package io.github.ilnurnasybullin.tagfm.repository.xml.entity;

import io.github.ilnurnasybullin.tagfm.core.repository.TaggedFile;

import javax.xml.bind.annotation.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;

@XmlRootElement(name = "tagged-file")
public class TaggedFileEntity implements TaggedFile {

    private String name;
    private Set<TagEntity> tags;

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaggedFileEntity that = (TaggedFileEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @XmlElementWrapper(name = "tags")
    @XmlElement(name = "tag")
    @XmlIDREF
    public Set<TagEntity> getTags() {
        return tags;
    }

    public void setTags(Set<TagEntity> tags) {
        this.tags = tags;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TagEntity> tags() {
        return getTags();
    }

    @Override
    public Path file() {
        return Paths.get(name);
    }
}
