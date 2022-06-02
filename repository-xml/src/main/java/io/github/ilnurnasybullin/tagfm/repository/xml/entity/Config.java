package io.github.ilnurnasybullin.tagfm.repository.xml.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement
@XmlType(name = "config", propOrder = {
        "workingNamespaceName",
        "namespaces"
})
public class Config {

    private String workingNamespaceName;
    private Set<String> namespaces;

    public Config() {
        this.namespaces = new HashSet<>();
    }

    @XmlElement(name = "working-namespace", required = true)
    public String getWorkingNamespaceName() {
        return workingNamespaceName;
    }

    public void setWorkingNamespaceName(String workingNamespaceName) {
        this.workingNamespaceName = workingNamespaceName;
    }

    @XmlElementWrapper(name = "namespaces")
    @XmlElement(name = "namespace")
    public Set<String> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(Set<String> namespaces) {
        this.namespaces = namespaces;
    }
}
