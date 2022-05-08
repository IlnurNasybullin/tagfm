package io.github.ilnurnasybullin.tagfm.cli.beans;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceServiceImpl;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

@Factory
public class JavaBeans {

    @Bean
    public NamespaceServiceImpl namespaceService() {
        return new NamespaceServiceImpl();
    }

}
