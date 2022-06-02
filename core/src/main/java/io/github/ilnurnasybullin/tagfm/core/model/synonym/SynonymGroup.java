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

package io.github.ilnurnasybullin.tagfm.core.model.synonym;

import io.github.ilnurnasybullin.tagfm.core.api.dto.SynonymGroupView;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.util.*;

public final class SynonymGroup implements SynonymGroupView {

    private final Set<TreeTag> tags;

    public SynonymGroup() {
        this(new ArrayList<>());
    }

    public SynonymGroup(Collection<TreeTag> tags) {
        this.tags = new HashSet<>(tags);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<TreeTag> tags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SynonymGroup that = (SynonymGroup) o;
        return Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tags);
    }
}
