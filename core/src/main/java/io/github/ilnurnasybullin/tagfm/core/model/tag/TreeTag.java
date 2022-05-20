package io.github.ilnurnasybullin.tagfm.core.model.tag;

import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Ilnur Nasybullin
 */
public sealed class TreeTag implements Tag permits TreeTagSafety {

    protected final static String SEPARATOR = "/";
    
    private String name;
    private String fullName;
    private TreeTag parent;
    private final Map<String, TreeTag> children;

    protected TreeTag(String name, String fullName, TreeTag parent, Map<String, TreeTag> children) {
        this.name = name;
        this.parent = parent;
        this.fullName = fullName;
        this.children = children;
    }

    protected static String fullName(TreeTag parent, String name) {
        return parent == null ? name : String.format("%s%s%s", parent.fullName(), SEPARATOR, name);
    }

    protected String calculateFullName(TreeTag parent, String name) {
        return fullName(parent, name);
    }

    @Override
    public void rename(String newName) {
        this.name = newName;
        this.fullName = calculateFullName(parent, newName);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    public void reparent(TreeTag newParent) {
        this.parent = newParent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<TreeTag> parent() {
        return Optional.ofNullable(parent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, TreeTag> children() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeTag treeTag = (TreeTag) o;
        return Objects.equals(fullName, treeTag.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName);
    }

    @Override
    public String toString() {
        return String.format("TreeTag{fullName=[%s]}", fullName);
    }
    
}
