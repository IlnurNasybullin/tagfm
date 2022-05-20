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

import io.github.ilnurnasybullin.logical.expression.evaluator.LogicalASTree;
import io.github.ilnurnasybullin.tagfm.core.parser.LogicalExpressionEvaluator;
import io.github.ilnurnasybullin.tagfm.core.parser.LogicalExpressionParser;

import java.util.List;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
public class LogicalExpressionParserImpl<T> implements LogicalExpressionParser<T> {

    @Override
    public LogicalExpressionEvaluator<T> parse(List<String> tokens, Function<String, T> mapper) {
        LogicalASTree<T> evaluatorTree = getParser(mapper)
                .parse(tokens);
        return new LogicalExpressionEvaluatorImpl<>(evaluatorTree);
    }

    private io.github.ilnurnasybullin.logical.expression.parser.LogicalExpressionParser<T> getParser(Function<String, T> mapper) {
        return new io.github.ilnurnasybullin.logical.expression.parser.LogicalExpressionParser<>(mapper);
    }
}
