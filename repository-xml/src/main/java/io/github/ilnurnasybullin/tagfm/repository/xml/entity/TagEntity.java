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

package io.github.ilnurnasybullin.tagfm.repository.xml.entity;

import io.github.ilnurnasybullin.tagfm.core.repository.TagRepoDto;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@XmlRootElement(name = "tag")
public class TagEntity implements TagRepoDto {

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

    @Override
    @SuppressWarnings("unchecked")
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
