/*
 * Copyright 2022 Ilnur Nasybullin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
