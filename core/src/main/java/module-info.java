import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;

module tagged.file.manager.core {
    requires transitive tagged.file.manager.api;
    exports io.github.ilnurnasybullin.tagfm.core.repository
            to tagged.file.manager.repository.xml;

    exports io.github.ilnurnasybullin.tagfm.core.dto.file;
    exports io.github.ilnurnasybullin.tagfm.core.dto.tag;
    exports io.github.ilnurnasybullin.tagfm.core.dto.namespace;

    uses NamespaceRepository;
}