package io.github.ilnurnasybullin.tagfm.repository.xml.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "synonym")
public class SynonymsEntity {

    private Set<TagEntity> tags;

    @XmlElementWrapper
    @XmlElement(name = "tag")
    @XmlIDREF
    public Set<TagEntity> getTags() {
        return tags;
    }

    public void setTags(Set<TagEntity> tags) {
        this.tags = tags;
    }
}
