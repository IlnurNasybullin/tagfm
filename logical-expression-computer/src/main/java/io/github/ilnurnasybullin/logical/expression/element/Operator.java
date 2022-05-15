package io.github.ilnurnasybullin.logical.expression.element;

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
