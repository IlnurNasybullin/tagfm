package io.github.ilnurnasybullin.tagfm.repository.xml.entity;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum FileNamingStrategyEntity {
    @XmlEnumValue("absolute")
    ABSOLUTE,
    @XmlEnumValue("relative")
    RELATIVE;

    public static FileNamingStrategyEntity from(FileNamingStrategy strategy) {
        return switch (strategy) {
            case ABSOLUTE -> ABSOLUTE;
            case RELATIVE -> RELATIVE;
        };
    }

    public static FileNamingStrategy to(FileNamingStrategyEntity fileNaming) {
        return switch (fileNaming) {
            case ABSOLUTE -> FileNamingStrategy.ABSOLUTE;
            case RELATIVE -> FileNamingStrategy.RELATIVE;
        };
    }
}
