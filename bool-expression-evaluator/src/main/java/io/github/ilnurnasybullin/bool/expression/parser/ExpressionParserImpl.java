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

package io.github.ilnurnasybullin.bool.expression.parser;

import io.github.ilnurnasybullin.bool.expression.terms.*;
import io.github.ilnurnasybullin.bool.expression.tree.ExpressionTree;
import io.github.ilnurnasybullin.bool.expression.tree.ExpressionTreeImpl;
import io.github.ilnurnasybullin.bool.expression.tokenizer.ExpressionTokenizer;

import java.util.*;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
public class ExpressionParserImpl<T> implements ExpressionParser<T> {

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

    public ExpressionParserImpl(Function<String, T> mapper) {
        this.mapper = mapper;
    }

    // brackets aren't checking!
    @Override
    public ExpressionTree<T> parse(String expression) {
        List<ExpressionTree<T>> treeTerms = new ArrayList<>();
        List<String> tokens = getTokens(expression);

        Deque<Term<T>> terms = getTerms(tokens);

        ExpressionTree<T> root = null;
        for (Term<T> term: terms) {
            if (term.isOperand()) {
                root = ExpressionTreeImpl.operand(term);
                treeTerms.add(root);
                continue;
            }

            ListIterator<ExpressionTree<T>> iterator = treeTerms.listIterator(treeTerms.size());
            List<ExpressionTree<T>> operands = new ArrayList<>();
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
                                expression, operator.operator()
                        )
                );
            }

            root = ExpressionTreeImpl.operator(term, operands);
            treeTerms.add(root);
        }

        if (root == null) {
            throw new IllegalStateException(
                    String.format("Building logical AST not available for tokens [%s]", expression)
            );
        }

        return root;
    }

    private List<String> getTokens(String expression) {
        ExpressionTokenizer tokenizer = ExpressionTokenizer.getInstance();
        return tokenizer.tokenize(expression);
    }

    // infix to postfix (Shunting-Yard Algorithm)
    private Deque<Term<T>> getTerms(List<String> tokens) {
        int currentLevel = 0;
        Deque<Term<T>> terms = new ArrayDeque<>();
        Deque<OperatorElement> operators = new ArrayDeque<>();

        int leftToRight = 0;

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
                case NOT -> addOperator(new OperatorElement(Operator.NOT, currentLevel, leftToRight++), operators, terms);
                case AND -> addOperator(new OperatorElement(Operator.AND, currentLevel, leftToRight++), operators, terms);
                case OR -> addOperator(new OperatorElement(Operator.OR, currentLevel, leftToRight++), operators, terms);
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
