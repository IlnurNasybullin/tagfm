package io.github.ilnurnasybullin.tagfm.core.service;

import java.util.ServiceLoader;
import java.util.function.BiConsumer;

public interface Publisher {
    void addSubscriber(String propertyName, BiConsumer<Object, Object> subscriber);
    void removeSubscriber(String propertyName, BiConsumer<Object, Object> subscriber);
    void publish(String propertyName, Object oldValue, Object newValue);

    static Publisher get() {
        return ServiceLoader.load(Publisher.class)
                .findFirst()
                .orElseThrow();
    }

}
