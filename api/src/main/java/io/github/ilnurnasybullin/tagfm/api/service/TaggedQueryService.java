package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.stream.Stream;

public interface TaggedQueryService<TQ, TF> {
    Stream<TF> taggedFiles(TQ query);
}
