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

import io.github.ilnurnasybullin.tagfm.core.repository.Namespace;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;
import io.github.ilnurnasybullin.tagfm.repository.xml.entity.NamespaceEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

public class NamespaceRepositoryImpl implements NamespaceRepository {

    private final Path savingFile = Path.of(".tagfm/namespace.xml");

    @Override
    public Optional<Namespace> findBy() {
        if (Files.notExists(savingFile) || Files.isDirectory(savingFile) || !Files.isReadable(savingFile)) {
            return Optional.empty();
        }

        Optional<NamespaceEntity> optional = read(savingFile);
        return optional.map(entity -> new NamespaceMapper().to(entity));
    }

    private Optional<NamespaceEntity> read(Path savedPath) {
        if (Files.notExists(savedPath) || Files.isDirectory(savedPath)) {
            return Optional.empty();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(NamespaceEntity.class);
            NamespaceEntity entity = (NamespaceEntity) context.createUnmarshaller().unmarshal(savedPath.toFile());
            return Optional.of(entity);
        } catch (JAXBException e) {
            throw new IllegalStateException("Problems with creating JAXBContext", e);
        }
    }

    @Override
    public void commit(Namespace namespace) {
        NamespaceEntity entity = new NamespaceMapper().from(namespace);
        save(savingFile, entity);
    }



    private void save(Path savingFile, NamespaceEntity entity) {
        try(FileTransactionManager manager = new FileTransactionManager(savingFile)) {
            JAXBContext context = JAXBContext.newInstance(NamespaceEntity.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(entity, savingFile.toFile());
            manager.acknowledge();
        } catch (JAXBException e) {
            throw new IllegalStateException("Problems with creating JAXBContext", e);
        }
    }
}
