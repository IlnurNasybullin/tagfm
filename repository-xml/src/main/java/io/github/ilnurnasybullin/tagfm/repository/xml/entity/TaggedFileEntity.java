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
