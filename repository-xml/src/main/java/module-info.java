import io.github.ilnurnasybullin.tagfm.repository.xml.repository.NamespaceRepositoryImpl;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;

module tagged.file.manager.repository.xml {
    requires tagged.file.manager.core;

    requires java.activation;
    requires java.xml.bind;
    requires com.sun.xml.bind;

    exports io.github.ilnurnasybullin.tagfm.repository.xml.repository;
    exports io.github.ilnurnasybullin.tagfm.repository.xml.entity;

    provides NamespaceRepository with NamespaceRepositoryImpl;

    opens io.github.ilnurnasybullin.tagfm.repository.xml.entity;
}