package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.*;

public class SynonymTagManager {

    public void bindSynonyms(Collection<TreeTagDto> tags, NamespaceDto namespace) {
        if (tags.isEmpty() || tags.size() == 1) {
            return;
        }

        Set<TreeTagDto> bindTags = Collections.newSetFromMap(new IdentityHashMap<>());
        bindTags.addAll(tags);

        List<Set<TreeTagDto>> namespaceSynonyms = namespace.synonyms();
        List<Set<TreeTagDto>> synonymsList = namespaceSynonyms
                .stream()
                .filter(synonyms -> !Collections.disjoint(synonyms, bindTags))
                .toList();

        synonymsList.forEach(synonyms -> {
            bindTags.addAll(synonyms);
            namespaceSynonyms.remove(synonyms);
        });

        namespaceSynonyms.add(bindTags);
    }

    public void unbindSynonyms(Collection<TreeTagDto> tags, NamespaceDto namespace) {
        if (tags.isEmpty()) {
            return;
        }

        List<Set<TreeTagDto>> removingSynonyms = namespace.synonyms()
                .stream()
                .filter(synonyms -> synonyms.removeAll(tags) && synonyms.size() < 2)
                .toList();

        namespace.synonyms().removeAll(removingSynonyms);
    }

}
