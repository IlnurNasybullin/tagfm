package io.github.ilnurnasybullin.tagfm.repository.xml.entity;

import io.github.ilnurnasybullin.tagfm.core.repository.Tag;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@XmlRootElement(name = "tag")
public class TagEntity implements Tag {

    private long id;
    private String name;
    private TagEntity parent;

    @XmlTransient
    private final Set<TagEntity> children;

    public TagEntity() {
        children = new HashSet<>();
    }

    public TagEntity(long id) {
        this();
        this.id = id;
    }

    public static TagEntity createWithId(long id) {
        return new TagEntity(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    @XmlIDREF
    @XmlAttribute
    public TagEntity getParent() {
        return parent;
    }

    public void setParent(TagEntity parent) {
        this.parent = parent;
        if (parent != null) {
            parent.children().add(this);
        }
    }

    public Set<TagEntity> children() {
        return children;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return Long.toString(id);
    }

    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    @Override
    public String name() {
        return getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<TagEntity> parent() {
        return Optional.ofNullable(parent);
    }
}
