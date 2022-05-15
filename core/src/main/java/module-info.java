import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;
import io.github.ilnurnasybullin.tagfm.core.search.LogicalExpressionParser;

module tagfm.core {
    requires transitive tagfm.api;

    exports io.github.ilnurnasybullin.tagfm.core.repository;

    exports io.github.ilnurnasybullin.tagfm.core.dto.file;
    exports io.github.ilnurnasybullin.tagfm.core.dto.tag;
    exports io.github.ilnurnasybullin.tagfm.core.dto.namespace;
    exports io.github.ilnurnasybullin.tagfm.core.dto.synonym;

    exports io.github.ilnurnasybullin.tagfm.core.iterator;

    exports io.github.ilnurnasybullin.tagfm.core.search;

    uses NamespaceRepository;
    uses LogicalExpressionParser;
}