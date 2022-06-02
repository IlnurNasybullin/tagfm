package io.github.ilnurnasybullin.tagfm.core.model.synonym;

import io.github.ilnurnasybullin.tagfm.core.api.dto.SynonymGroupView;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTagRepoDto;
import io.github.ilnurnasybullin.tagfm.core.repository.SynonymGroupEntity;
import io.github.ilnurnasybullin.tagfm.core.repository.TagEntity;

import java.util.Set;
import java.util.stream.Collectors;

public final class SynonymGroupRepoDto implements SynonymGroupEntity {

    private final SynonymGroupView synonymGroup;

    private SynonymGroupRepoDto(SynonymGroupView synonymGroup) {
        this.synonymGroup = synonymGroup;
    }

    public static SynonymGroupRepoDto of(SynonymGroupView synonymGroup) {
        return new SynonymGroupRepoDto(synonymGroup);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TagEntity> tags() {
        return synonymGroup.tags().stream()
                .map(TreeTagRepoDto::of)
                .collect(Collectors.toSet());
    }
}
