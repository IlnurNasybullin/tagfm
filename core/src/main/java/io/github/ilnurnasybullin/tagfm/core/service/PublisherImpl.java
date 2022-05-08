package io.github.ilnurnasybullin.tagfm.core.service;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class PublisherImpl implements Publisher {

    private final Map<String, Set<BiConsumer<Object, Object>>> subscribersMap;

    public PublisherImpl() {
        this.subscribersMap = new IdentityHashMap<>();
    }

    @Override
    public void addSubscriber(String propertyName, BiConsumer<Object, Object> subscriber) {
        subscribersMap.computeIfAbsent(propertyName, key -> Collections.newSetFromMap(new IdentityHashMap<>()))
                .add(subscriber);
    }

    @Override
    public void removeSubscriber(String propertyName, BiConsumer<Object, Object> subscriber) {
        Set<BiConsumer<Object, Object>> subscribers = subscribersMap.get(propertyName);
        if (subscribers != null) {
            subscribers.remove(subscriber);
        }
    }

    @Override
    public void publish(String propertyName, Object oldValue, Object newValue) {
        Set<BiConsumer<Object, Object>> subscribers = subscribersMap.get(propertyName);
        if (subscribers != null) {
            subscribers.forEach(subscriber -> subscriber.accept(oldValue, newValue));
        }
    }
}
