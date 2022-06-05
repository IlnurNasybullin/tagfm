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

package io.github.ilnurnasybullin.tagfm.logical.expression;

import io.github.ilnurnasybullin.logical.expression.evaluator.BooleanExpressionTree;
import io.github.ilnurnasybullin.tagfm.core.evaluator.BooleanExpressionEvaluator;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
public class BooleanExpressionEvaluatorImpl<T> implements BooleanExpressionEvaluator<T> {

    private final BooleanExpressionTree<T> evaluatorTree;

    public BooleanExpressionEvaluatorImpl(BooleanExpressionTree<T> evaluatorTree) {
        this.evaluatorTree = evaluatorTree;
    }

    @Override
    public boolean evaluate(Predicate<T> mapper) {
        return evaluatorTree.evaluate(mapper);
    }

    @Override
    public <U> BooleanExpressionEvaluator<U> map(Function<T, U> mapper) {
        return new BooleanExpressionEvaluatorImpl<>(evaluatorTree.map(mapper));
    }
}
