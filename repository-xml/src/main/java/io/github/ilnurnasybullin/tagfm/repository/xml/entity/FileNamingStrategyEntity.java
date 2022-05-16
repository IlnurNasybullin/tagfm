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
