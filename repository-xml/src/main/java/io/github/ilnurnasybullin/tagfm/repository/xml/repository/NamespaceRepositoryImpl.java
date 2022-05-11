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

    public Optional<NamespaceEntity> read(Path savedPath) {
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

    private void initPath() {
        if (Files.notExists(savingFile)) {
            createFiles(savingFile);
        }
    }

    private void removePath() {
        try {
            Files.walk(savingFile.getParent())
                    .filter(Files::exists)
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder())
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void save(Path savingFile, NamespaceEntity entity) {
        try(TransactionManager manager = new TransactionManager(this::initPath, this::removePath)) {
            JAXBContext context = JAXBContext.newInstance(NamespaceEntity.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(entity, savingFile.toFile());
            manager.acknowledge();
        } catch (JAXBException e) {
            throw new IllegalStateException("Problems with creating JAXBContext", e);
        }
    }

    private void createFiles(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (IOException e) {
            throw new NestedFilesCreatingException(
                    String.format("Problems with creating nested paths of [%s]", path), e
            );
        }
    }
}
