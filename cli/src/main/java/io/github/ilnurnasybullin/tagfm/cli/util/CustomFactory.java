package io.github.ilnurnasybullin.tagfm.cli.util;

import io.micronaut.context.ApplicationContext;
import picocli.CommandLine;

import java.util.Collection;

public class CustomFactory implements CommandLine.IFactory {

    private final ApplicationContext context;
    private final CommandLine.IFactory factory;

    public CustomFactory(ApplicationContext context, CommandLine.IFactory factory) {
        this.context = context;
        this.factory = factory;
    }

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        return Collection.class.isAssignableFrom(cls) ? factory.create(cls) : context.getBean(cls);
    }
}
