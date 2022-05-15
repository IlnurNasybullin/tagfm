package io.github.ilnurnasybullin.logical.expression.parser;

import io.github.ilnurnasybullin.logical.expression.evaluator.LogicalASTree;
import io.github.ilnurnasybullin.logical.expression.evaluator.LogicalASTreeImpl;
import io.github.ilnurnasybullin.logical.expression.element.*;

import java.util.*;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
public class LogicalExpressionParser<T> implements LogicalParser<T> {

    private final static Map<String, Token> tokenMap;

    static {
        tokenMap = Map.of(
                "(", Token.OPEN_BRACKET,
                ")", Token.CLOSE_BRACKET,
                "&", Token.AND,
                "~", Token.NOT,
                "|", Token.OR
        );
    }

    private final Function<String, T> mapper;

    public LogicalExpressionParser(Function<String, T> mapper) {
        this.mapper = mapper;
    }

    // brackets aren't checking!
    @Override
    public LogicalASTree<T> parse(List<String> tokens) {
        List<LogicalASTree<T>> treeTerms = new ArrayList<>();

        Deque<Term<T>> terms = getTerms(tokens);

        LogicalASTree<T> root = null;
        for (Term<T> term: terms) {
            if (term.isOperand()) {
                treeTerms.add(LogicalASTreeImpl.operand(term));
                continue;
            }

            ListIterator<LogicalASTree<T>> iterator = treeTerms.listIterator(treeTerms.size());
            List<LogicalASTree<T>> operands = new ArrayList<>();
            OperatorElement operator = (OperatorElement) term;

            int i = 0;
            while (iterator.hasPrevious() && i < operator.operator().operandsCount()) {
                operands.add(iterator.previous());
                iterator.remove();
                i++;
            }

            if (i < operator.operator().operandsCount()) {
                throw new IllegalTokensExpression(
                        String.format(
                                "Illegal tokens expression [%s]: invalid operands count for operator [%s]",
                                tokens, operator.operator()
                        )
                );
            }

            root = LogicalASTreeImpl.operator(term, operands);
            treeTerms.add(root);
        }

        if (root == null) {
            throw new IllegalStateException(
                    String.format("Building logical AST not available for tokens [%s]", tokens)
            );
        }

        return root;
    }

    private Deque<Term<T>> getTerms(List<String> tokens) {
        int currentLevel = 0;
        Deque<Term<T>> terms = new ArrayDeque<>();
        Deque<OperatorElement> operators = new ArrayDeque<>();

        for (String tokenString : tokens) {
            Token token = tokenMap.get(tokenString);
            if (token == null) {
                terms.addLast(new OperandElement<>(mapper.apply(tokenString)));
                continue;
            }

            switch (token) {
                case OPEN_BRACKET -> currentLevel--;
                case CLOSE_BRACKET -> {
                    currentLevel++;
                    if (currentLevel > 0) {
                        throw ibcException(tokens);
                    }
                }
                case NOT -> addOperator(new OperatorElement(Operator.NOT, currentLevel), operators, terms);
                case AND -> addOperator(new OperatorElement(Operator.AND, currentLevel), operators, terms);
                case OR -> addOperator(new OperatorElement(Operator.OR, currentLevel), operators, terms);
            }
        }

        if (currentLevel != 0) {
            throw ibcException(tokens);
        }

        Iterator<OperatorElement> operatorsIterator = operators.descendingIterator();
        while (operatorsIterator.hasNext()) {
            @SuppressWarnings("unchecked")
            Term<T> operator = operatorsIterator.next();
            terms.addLast(operator);
        }

        return terms;
    }

    private void addOperator(OperatorElement operator, Deque<OperatorElement> operators, Deque<Term<T>> terms) {
        OperatorElement lastOperator;
        while ((lastOperator = operators.peekLast()) != null &&
                lastOperator.priorityThan(operator)) {
            operators.removeLast();

            @SuppressWarnings("unchecked")
            Term<T> term = lastOperator;
            terms.addLast(term);
        }

        operators.addLast(operator);
    }

    private IllegalBracketsCountException ibcException(List<String> tokens) {
        return new IllegalBracketsCountException(
                String.format("Illegal bracket counts in tokens [%s]", tokens)
        );
    }

}
