import io.github.ilnurnasybullin.tagfm.repository.xml.repository.NamespaceRepositoryImpl;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;

module tagfm.repository.xml {
    requires tagfm.core;

    requires java.activation;
    requires java.xml.bind;
    requires com.sun.xml.bind;

    exports io.github.ilnurnasybullin.tagfm.repository.xml.repository;
    exports io.github.ilnurnasybullin.tagfm.repository.xml.entity;

    provides NamespaceRepository with NamespaceRepositoryImpl;

    opens io.github.ilnurnasybullin.tagfm.repository.xml.entity;
}