package io.github.ilnurnasybullin.logical.expression.element;

/**
 * @author Ilnur Nasybullin
 */
public interface Term<T> {

    boolean isOperator();
    default boolean isOperand() {
        return !isOperator();
    }

    Operator operator();
    T operand();

}
