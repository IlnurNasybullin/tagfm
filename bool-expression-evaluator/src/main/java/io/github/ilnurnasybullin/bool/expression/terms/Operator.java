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

package io.github.ilnurnasybullin.bool.expression.terms;

/**
 * @author Ilnur Nasybullin
 */
public enum Operator {
    NOT(0, 1),
    AND(1, 2),
    OR(2, 2);

    private final int priorityLevel;
    private final int operandsCount;

    Operator(int priorityLevel, int operandsCount) {
        this.priorityLevel = priorityLevel;
        this.operandsCount = operandsCount;
    }

    public int priorityLevel() {
        return priorityLevel;
    }

    public int operandsCount() {
        return operandsCount;
    }
}
