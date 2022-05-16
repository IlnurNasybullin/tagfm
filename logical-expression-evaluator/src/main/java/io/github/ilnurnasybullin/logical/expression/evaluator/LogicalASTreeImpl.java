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

package io.github.ilnurnasybullin.logical.expression.evaluator;

import io.github.ilnurnasybullin.logical.expression.element.OperandElement;
import io.github.ilnurnasybullin.logical.expression.element.Term;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
public class LogicalASTreeImpl<T> implements LogicalASTree<T> {

    private final static Comparator<LogicalASTree<?>> optimizeComparator =
            Comparator.comparingInt(LogicalASTree::leafsCount);

    private final int leafsCount;
    private final Term<T> term;
    private final PriorityQueue<LogicalASTree<T>> leafs;

    private LogicalASTreeImpl(Term<T> term, PriorityQueue<LogicalASTree<T>> leafs, int leafsCount) {
        this.term = term;
        this.leafs = leafs;
        this.leafsCount = leafsCount;
    }

    public static <T> LogicalASTree<T> operand(Term<T> operand) {
        return of(operand, List.of());
    }

    public static <T> LogicalASTree<T> operator(Term<T> operator, Collection<LogicalASTree<T>> operands) {
        return of(operator, operands);
    }

    private static <T> LogicalASTree<T> of(Term<T> term, Collection<LogicalASTree<T>> leafs) {
        PriorityQueue<LogicalASTree<T>> prioritizedLeafs = new PriorityQueue<>(optimizeComparator);
        prioritizedLeafs.addAll(leafs);

        int leafsCount = prioritizedLeafs.stream()
                .mapToInt(LogicalASTree::leafsCount)
                .sum() + prioritizedLeafs.size();

        return new LogicalASTreeImpl<>(term, prioritizedLeafs, leafsCount);
    }

    @Override
    public int leafsCount() {
        return leafsCount;
    }

    @Override
    public <U> LogicalASTree<U> map(Function<T, U> mapFunction) {
        if (leafsCount == 0) {
            U mappedElement = mapFunction.apply(term.operand());
            return operand(new OperandElement<>(mappedElement));
        }

        List<LogicalASTree<U>> leafs = this.leafs.stream()
                .map(tree -> tree.map(mapFunction))
                .toList();

        @SuppressWarnings("unchecked")
        Term<U> mappedTerm = (Term<U>) term;

        return operator(mappedTerm, leafs);
    }

    @Override
    public boolean evaluate(Predicate<T> mapper) {
        if (leafsCount == 0) {
            return mapper.test(term.operand());
        }

        return switch (term.operator()) {
            case NOT -> !Objects.requireNonNull(leafs.peek()).evaluate(mapper);
            case AND -> evaluateAnd(mapper);
            case OR -> evaluateOr(mapper);
        };
    }

    private boolean evaluateOr(Predicate<T> mapper) {
        boolean result = false;
        Iterator<LogicalASTree<T>> iterator = leafs.iterator();
        while (!result && iterator.hasNext()) {
            result = iterator.next().evaluate(mapper);
        }
        return result;
    }

    private boolean evaluateAnd(Predicate<T> mapper) {
        boolean result = true;
        Iterator<LogicalASTree<T>> iterator = leafs.iterator();
        while (result && iterator.hasNext()) {
            result = iterator.next().evaluate(mapper);
        }
        return result;
    }

}
