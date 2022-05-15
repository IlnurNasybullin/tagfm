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
