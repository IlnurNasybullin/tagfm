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

package io.github.ilnurnasybullin.logical.expression.element;

/**
 * @author Ilnur Nasybullin
 */
public class OperatorElement implements Term, Comparable<OperatorElement> {

    private final Operator operator;
    private final int outerPriorityLevel;

    public OperatorElement(Operator operator, int outerPriorityLevel) {
        this.operator = operator;
        this.outerPriorityLevel = outerPriorityLevel;
    }

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public Operator operator() {
        return operator;
    }

    @Override
    public Object operand() {
        throw new UnsupportedOperationException(String.format("Operator [%s] isn't operand!", operator()));
    }

    @Override
    public int compareTo(OperatorElement other) {
        if (outerPriorityLevel == other.outerPriorityLevel) {
            return operator.priorityLevel() - other.operator().priorityLevel();
        }

        return outerPriorityLevel - other.outerPriorityLevel;
    }

    public boolean priorityThan(OperatorElement other) {
        return compareTo(other) <= 0;
    }
}
