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

package io.github.ilnurnasybullin.tagfm.repository.xml.repository;

import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceEntity;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;
import io.github.ilnurnasybullin.tagfm.repository.xml.entity.Config;
import io.github.ilnurnasybullin.tagfm.repository.xml.entity.Namespace;
import io.github.ilnurnasybullin.tagfm.repository.xml.fileManager.FilesTransactionManager;
import io.github.ilnurnasybullin.tagfm.repository.xml.fileManager.TransactionState;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class NamespaceRepositoryImpl implements NamespaceRepository {

    @Override
    public Optional<NamespaceEntity> findBy(String name) {
        if (name == null) {
            return Optional.empty();
        }

        Path path = GlobalConstants.NAMESPACE_RESOLVER.apply(name);
        Optional<Namespace> optional = readXml(path, Namespace.class);
        return optional.map(entity -> new NamespaceMapper().to(entity));
    }

    @Override
    public List<NamespaceEntity> getAll() {
        Path path = GlobalConstants.CONFIG_FILE;
        return readXml(path, Config.class)
                .map(Config::getNamespaces)
                .map(list -> list.stream()
                        .flatMap(name -> findBy(name)
                                .stream())
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }

    @Override
    public void replace(String name, NamespaceEntity namespace) {
        checkOnExisting(name);

        if (name.equals(namespace.name())) {
            commit(namespace);
            return;
        }

        commit(name, namespace);
    }

    private void checkOnExisting(String name) {
        if (Files.notExists(GlobalConstants.ROOT_DIR)) {
            throw instanceNEE(name);
        }
    }

    private NamespaceNotExistingException instanceNEE(String name) {
        return new NamespaceNotExistingException(String.format("Namespace [%s] isn't existing!", name));
    }

    private <T> Optional<T> readXml(Path savedPath, Class<T> tClass) {
        if (Files.notExists(savedPath) || Files.isDirectory(savedPath)) {
            return Optional.empty();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(tClass);
            T entity = tClass.cast(context.createUnmarshaller()
                    .unmarshal(savedPath.toFile()));
            return Optional.of(entity);
        } catch (JAXBException e) {
            throw new IllegalStateException("Problems with creating JAXBContext", e);
        }
    }

    @Override
    public void commit(NamespaceEntity namespace) {
        commit(namespace.name(), namespace);
    }

    private void commit(String fileName, NamespaceEntity namespace) {
        Path savingFile = GlobalConstants.NAMESPACE_RESOLVER.apply(fileName);
        try {
            Files.createDirectories(GlobalConstants.ROOT_DIR);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Namespace entity = new NamespaceMapper().from(namespace);
        save(savingFile, entity);

        Config config = readXml(GlobalConstants.CONFIG_FILE, Config.class)
                .orElseGet(Config::new);

        if (config.getNamespaces().add(entity.getName())) {
            save(GlobalConstants.CONFIG_FILE, config);
        }
    }

    @Override
    public void remove(NamespaceEntity namespace) {
        String namespaceName = namespace.name();
        checkOnExisting(namespaceName);
        readXml(GlobalConstants.CONFIG_FILE, Config.class)
                .ifPresent(config -> {
                    if (config.getNamespaces().remove(namespaceName)) {
                        try {
                            Files.deleteIfExists(GlobalConstants.NAMESPACE_RESOLVER.apply(namespaceName));
                            if (Objects.equals(config.getWorkingNamespaceName(), namespaceName)) {
                                config.setWorkingNamespaceName(null);
                            }
                            save(GlobalConstants.CONFIG_FILE, config);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    } else {
                        throw instanceNEE(namespaceName);
                    }
                });
    }

    @Override
    public Optional<NamespaceEntity> getWorkingNamespace() {
        Path path = GlobalConstants.CONFIG_FILE;
        return readXml(path, Config.class)
                .map(Config::getWorkingNamespaceName)
                .flatMap(this::findBy);
    }

    @Override
    public void setWorkingNamespace(String name) {
        checkOnExisting(name);

        Path path = GlobalConstants.CONFIG_FILE;
        if (Files.notExists(path)) {
            throw instanceNEE(name);
        }

        readXml(path, Config.class)
                .ifPresent(config -> {
                    if (config.getNamespaces().contains(name)) {
                        config.setWorkingNamespaceName(name);
                        save(GlobalConstants.CONFIG_FILE, config);
                    } else {
                        throw instanceNEE(name);
                    }
                });
    }

    private <T> void save(Path savingFile, T entity) {
        FilesTransactionManager transactionManager = new FilesTransactionManager();
        transactionManager.transactionCreate(savingFile, () -> {
            Class<?> tClass = entity.getClass();
            JAXBContext context = JAXBContext.newInstance(tClass);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(entity, savingFile.toFile());

            return TransactionState.COMPLETED;
        });
    }
}
