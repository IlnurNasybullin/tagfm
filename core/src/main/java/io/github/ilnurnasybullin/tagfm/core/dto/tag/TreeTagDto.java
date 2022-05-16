/*
 * Copyright 2022 Ilnur Nasybullin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TreeTagDto extends AbstractTreeTag {

    private String name;
    private String fullName;
    private TreeTagDto parent;
    private final Map<String, TreeTagDto> children;

    protected TreeTagDto(String name, String fullName, TreeTagDto parent, Map<String, TreeTagDto> children) {
        this.name = name;
        this.parent = parent;
        this.fullName = fullName;
        this.children = children;
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

    @Override
    public void reparent(TreeTagDto newParent) {
        this.parent = newParent;
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
