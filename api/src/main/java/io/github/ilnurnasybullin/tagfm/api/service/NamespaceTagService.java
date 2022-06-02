package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ilnur Nasybullin
 */
public interface NamespaceTagService<T> {
    Optional<T> findByFullName(String fullName);
    Map<String, Optional<T>> findByFullNames(Collection<String> names);
    Stream<T> findByName(String name);
    Map<String, List<T>> findByNames(Collection<String> names);

    T findOrCreateByFullName(String fullName);
    T findOrCreateByNameExact(String name);

    default Map<String, T> findOrCreateByFullNames(Collection<String> names) {
        return names.stream()
                .map(name -> Map.entry(name, findOrCreateByFullName(name)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    default Map<String, T> findOrCreateByNamesExact(Collection<String> names) {
        return names.stream()
                .map(name -> Map.entry(name, findOrCreateByNameExact(name)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private T getExactValue(String tagName, List<T> values) {
        if (values.isEmpty()) {
            throw tagNotFound(tagName);
        }

        if (values.size() > 2) {
            throw new NonUniqueTagNameException(
                    String.format("Tag with name [%s] isn't unique in namespace that has tags %s!", tagName, values)
            );
        }

        return values.get(0);
    }

    private TagNotFoundException tagNotFound(String tagName) {
        return new TagNotFoundException(String.format("Tag [%s] not found in namespace!", tagName));
    }

    default T findByNameExact(String name) {
        List<T> tags = findByName(name).toList();
        return getExactValue(name, tags);
    }

    default T findByFullNameExact(String fullName) {
        return findByFullName(fullName).orElseThrow(() -> tagNotFound(fullName));
    }

    default Map<String, T> findByNamesExact(Collection<String> names) {
        Map<String, List<T>> foundTags = findByNames(names);
        return names.stream()
                .map(name -> Map.entry(name, getExactValue(name, foundTags.getOrDefault(name, List.of()))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    default Map<String, T> findByFullNamesExact(Collection<String> names) {
        Map<String, Optional<T>> foundTags = findByFullNames(names);
        return names.stream()
                .map(name ->
                        Map.entry(name, foundTags.getOrDefault(
                                name, Optional.empty()
                        ).orElseThrow(() -> tagNotFound(name)))
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
