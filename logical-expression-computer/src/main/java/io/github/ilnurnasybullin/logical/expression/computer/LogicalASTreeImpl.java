package io.github.ilnurnasybullin.logical.expression.computer;

import io.github.ilnurnasybullin.logical.expression.element.Term;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
public class LogicalASTreeImpl<T> implements LogicalASTree<T> {

    private final static Comparator<LogicalASTreeImpl<?>> optimizeComparator =
            Comparator.comparingInt(LogicalASTreeImpl::leafsCount);

    private final int leafsCount;
    private final Term<T> term;
    private final PriorityQueue<LogicalASTreeImpl<T>> leafs;

    private LogicalASTreeImpl(Term<T> term, Collection<LogicalASTreeImpl<T>> leafs) {
        this.term = term;
        this.leafs = new PriorityQueue<>(optimizeComparator);
        this.leafs.addAll(leafs);

        leafsCount = leafs.stream()
                .mapToInt(LogicalASTreeImpl::leafsCount)
                .sum() + leafs.size();
    }

    public static <T> LogicalASTreeImpl<T> operand(Term<T> operand) {
        return new LogicalASTreeImpl<>(operand, List.of());
    }

    public static <T> LogicalASTreeImpl<T> operator(Term<T> operator, List<LogicalASTreeImpl<T>> operands) {
        return new LogicalASTreeImpl<>(operator, List.copyOf(operands));
    }

    public int leafsCount() {
        return leafsCount;
    }

    @Override
    public boolean compute(Predicate<T> mapper) {
        if (leafsCount == 0) {
            return mapper.test(term.operand());
        }

        return switch (term.operator()) {
            case NOT -> !Objects.requireNonNull(leafs.peek()).compute(mapper);
            case AND -> computeAnd(mapper);
            case OR -> computeOr(mapper);
        };
    }

    private boolean computeOr(Predicate<T> mapper) {
        boolean result = false;
        Iterator<LogicalASTreeImpl<T>> iterator = leafs.iterator();
        while (!result && iterator.hasNext()) {
            result = iterator.next().compute(mapper);
        }
        return result;
    }

    private boolean computeAnd(Predicate<T> mapper) {
        boolean result = true;
        Iterator<LogicalASTreeImpl<T>> iterator = leafs.iterator();
        while (result && iterator.hasNext()) {
            result = iterator.next().compute(mapper);
        }
        return result;
    }

}
