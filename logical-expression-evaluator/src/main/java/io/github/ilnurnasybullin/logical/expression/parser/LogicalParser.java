package io.github.ilnurnasybullin.logical.expression.parser;

import io.github.ilnurnasybullin.logical.expression.evaluator.LogicalASTree;

import java.util.List;

/**
 * @author Ilnur Nasybullin
 */
public interface LogicalParser<T> {
    // brackets aren't checking!
    LogicalASTree<T> parse(List<String> tokens);
}
