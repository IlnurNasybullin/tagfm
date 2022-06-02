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

import javax.xml.bind.annotation.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@XmlRootElement
@XmlType(name = "namespace", propOrder = {
        "tags",
        "synonymGroups",
        "taggedFiles"
})
public class Namespace {

    private String name;
    private FileNamingStrategyEntity fileNaming;
    private ZonedDateTime created;

    private Set<Tag> tags;
    private Set<TaggedFile> taggedFiles;
    private List<SynonymGroup> synonymGroups;

    public Namespace() {
        tags = new HashSet<>();
        taggedFiles = new HashSet<>();
        synonymGroups = new ArrayList<>();
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    @XmlElement(name = "tag", type = Tag.class)
    @XmlElementWrapper(name = "tags")
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @XmlElement(name = "tagged-file")
    @XmlElementWrapper(name = "tagged-files")
    public Set<TaggedFile> getTaggedFiles() {
        return taggedFiles;
    }

    public void setTaggedFiles(Set<TaggedFile> taggedFiles) {
        this.taggedFiles = taggedFiles;
    }

    @XmlAttribute
    public FileNamingStrategyEntity getFileNaming() {
        return fileNaming;
    }

    public void setFileNaming(FileNamingStrategyEntity fileNaming) {
        this.fileNaming = fileNaming;
    }

    @XmlAttribute
    public String getCreated() {
        return created == null ? null : created.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public ZonedDateTime created() {
        return created;
    }

    public void setCreated(String created) {
        this.created = ZonedDateTime.parse(created, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    @XmlElement(name = "tags")
    @XmlElementWrapper(name = "synonym-groups")
    public List<SynonymGroup> getSynonymGroups() {
        return synonymGroups;
    }

    public void setSynonymGroups(List<SynonymGroup> synonymGroups) {
        this.synonymGroups = synonymGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Namespace entity = (Namespace) o;
        return Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
