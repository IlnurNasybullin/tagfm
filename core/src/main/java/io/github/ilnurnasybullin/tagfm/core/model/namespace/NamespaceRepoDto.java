package io.github.ilnurnasybullin.tagfm.core.model.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.api.dto.SynonymGroupView;
import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFileRepoDto;
import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymGroupRepoDto;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTagRepoDto;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceEntity;
import io.github.ilnurnasybullin.tagfm.core.repository.SynonymGroupEntity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public class NamespaceRepoDto implements NamespaceEntity {

    private final NamespaceView namespace;

    private NamespaceRepoDto(NamespaceView namespace) {
        this.namespace = namespace;
    }

    public static NamespaceRepoDto of(NamespaceView namespace) {
        return new NamespaceRepoDto(namespace);
    }

    @Override
    public String name() {
        return namespace.name();
    }

    @Override
    public ZonedDateTime created() {
        return namespace.created();
    }

    @Override
    public FileNamingStrategy fileNaming() {
        return namespace.fileNaming();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TaggedFileRepoDto> files() {
        return namespace.files()
                .stream()
                .map(TaggedFileRepoDto::of)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SynonymGroupRepoDto> synonymGroups() {
        return namespace.synonymGroups()
                .stream()
                .map(SynonymGroupRepoDto::of)
                .toList();
    }

    @Override
    public TreeTagRepoDto root() {
        return TreeTagRepoDto.of(namespace.root());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamespaceRepoDto that = (NamespaceRepoDto) o;
        return Objects.equals(namespace, that.namespace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace);
    }
}
