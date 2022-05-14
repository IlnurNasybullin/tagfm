import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;

module tagfm.core {
    requires transitive tagfm.api;
    exports io.github.ilnurnasybullin.tagfm.core.repository;

    exports io.github.ilnurnasybullin.tagfm.core.dto.file;
    exports io.github.ilnurnasybullin.tagfm.core.dto.tag;
    exports io.github.ilnurnasybullin.tagfm.core.dto.namespace;
    exports io.github.ilnurnasybullin.tagfm.core.dto.synonym;

    uses NamespaceRepository;
}