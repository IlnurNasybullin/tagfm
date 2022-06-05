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

package io.github.ilnurnasybullin.bool.expression.tree;

import io.github.ilnurnasybullin.bool.expression.terms.OperandElement;
import io.github.ilnurnasybullin.bool.expression.terms.Term;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
public class ExpressionTreeImpl<T> implements ExpressionTree<T> {

    private final static Comparator<ExpressionTree<?>> optimizeComparator =
            Comparator.comparingInt(ExpressionTree::leafsCount);

    private final int leafsCount;
    private final Term<T> term;
    private final PriorityQueue<ExpressionTree<T>> leafs;

    private ExpressionTreeImpl(Term<T> term, PriorityQueue<ExpressionTree<T>> leafs, int leafsCount) {
        this.term = term;
        this.leafs = leafs;
        this.leafsCount = leafsCount;
    }

    public static <T> ExpressionTree<T> operand(Term<T> operand) {
        return of(operand, List.of());
    }

    public static <T> ExpressionTree<T> operator(Term<T> operator, Collection<ExpressionTree<T>> operands) {
        return of(operator, operands);
    }

    private static <T> ExpressionTree<T> of(Term<T> term, Collection<ExpressionTree<T>> leafs) {
        PriorityQueue<ExpressionTree<T>> prioritizedLeafs = new PriorityQueue<>(optimizeComparator);
        prioritizedLeafs.addAll(leafs);

        int leafsCount = prioritizedLeafs.stream()
                .mapToInt(ExpressionTree::leafsCount)
                .sum() + prioritizedLeafs.size();

        return new ExpressionTreeImpl<>(term, prioritizedLeafs, leafsCount);
    }

    @Override
    public int leafsCount() {
        return leafsCount;
    }

    @Override
    public <U> ExpressionTree<U> map(Function<T, U> mapFunction) {
        if (leafsCount == 0) {
            U mappedElement = mapFunction.apply(term.operand());
            return operand(new OperandElement<>(mappedElement));
        }

        List<ExpressionTree<U>> leafs = this.leafs.stream()
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
        Iterator<ExpressionTree<T>> iterator = leafs.iterator();
        while (!result && iterator.hasNext()) {
            result = iterator.next().evaluate(mapper);
        }
        return result;
    }

    private boolean evaluateAnd(Predicate<T> mapper) {
        boolean result = true;
        Iterator<ExpressionTree<T>> iterator = leafs.iterator();
        while (result && iterator.hasNext()) {
            result = iterator.next().evaluate(mapper);
        }
        return result;
    }

    public void printTree() {
        
    }

}
