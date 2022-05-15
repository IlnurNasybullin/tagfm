package io.github.ilnurnasybullin.logical.expression.element;

/**
 * @author Ilnur Nasybullin
 */
public class OperandElement<T> implements Term<T> {

    private final T operand;

    public OperandElement(T operand) {
        this.operand = operand;
    }

    @Override
    public boolean isOperator() {
        return false;
    }

    @Override
    public Operator operator() {
        throw new UnsupportedOperationException(String.format("Operand [%s] isn't operator!", operand));
    }

    @Override
    public T operand() {
        return operand;
    }
}
