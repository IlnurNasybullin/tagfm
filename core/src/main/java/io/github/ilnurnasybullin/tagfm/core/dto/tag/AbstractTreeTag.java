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

package io.github.ilnurnasybullin.tagfm.core.dto.tag;

public abstract class AbstractTreeTag implements TagDto {

    protected final static String SEPARATOR = "/";

    protected String calculateFullName(TreeTagDto parent, String name) {
        return fullName(parent, name);
    }

    protected static String fullName(TreeTagDto parent, String name) {
        return parent == null ? name : String.format("%s%s%s", parent.fullName(), SEPARATOR, name);
    }

}
