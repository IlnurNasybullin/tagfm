package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TreeTagDto extends AbstractTreeTag {

    private final String name;
    private final String fullName;
    private final TreeTagDto parent;
    private final Map<String, TreeTagDto> children;

    protected TreeTagDto(String name, String fullName, TreeTagDto parent, Map<String, TreeTagDto> children) {
        this.name = name;
        this.parent = parent;
        this.fullName = fullName;
        this.children = children;
    }

    @Override
    public TreeTagDto rename(String newName) {
        return new TreeTagDto(newName, calculateFullName(parent, newName), parent, children);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    protected TreeTagDto recalculateFullName() {
        return new TreeTagDto(name, calculateFullName(parent, name), parent, children);
    }

    @Override
    public TreeTagDto reparent(TreeTagDto newParent) {
        return new TreeTagDto(name, calculateFullName(newParent, name), newParent, children);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<TreeTagDto> parent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Map<String, TreeTagDto> children() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeTagDto treeTag = (TreeTagDto) o;
        return Objects.equals(fullName, treeTag.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName);
    }

    @Override
    public String toString() {
        return String.format("TreeTagDto{fullName=[%s]}", fullName);
    }
}
