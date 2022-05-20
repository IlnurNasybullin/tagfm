package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Ilnur Nasybullin
 */
public interface NamespaceTagFinderService<T> {
    Optional<T> findByFullName(String fullName);
    Stream<T> findByName(String name);
    Map<String, List<T>> findByNames(Collection<String> names);
    Map<String, T> findByFullNames(Collection<String> names);
}
