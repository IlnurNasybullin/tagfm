package io.github.ilnurnasybullin.tagfm.repository.xml;

import io.github.ilnurnasybullin.tagfm.repository.xml.entity.Config;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) throws JAXBException, IOException {
        Config config = new Config();
        config.getNamespaces().addAll(List.of("1", "2"));
        config.setWorkingNamespaceName("2");

        Class<Config> aClass = Config.class;
        JAXBContext context = JAXBContext.newInstance(aClass);
        System.out.println(context);
        Path example1 = Path.of("example");
        if (Files.notExists(example1)) {
            Files.createFile(example1);
        }

        File example = example1.toFile();
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(config, example);

        JAXBContext context1 = JAXBContext.newInstance(aClass);
        Config config1 = aClass.cast(context1.createUnmarshaller().unmarshal(example));
        config1.getNamespaces().forEach(System.out::println);
        System.out.println(config.getWorkingNamespaceName());
    }
}
