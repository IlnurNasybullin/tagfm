package io.github.ilnurnasybullin.logical.expression.tokenizer;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Function;

@FunctionalInterface
public interface LogicalTokenizer extends Function<String, List<String>> {

    @Override
    default List<String> apply(String expression) {
        return tokenize(expression);
    }

    List<String> tokenize(String expression);

    static LogicalTokenizer getInstance() {
        return ServiceLoader.load(LogicalTokenizer.class)
                .stream()
                .filter(provider -> provider.type() != SimpleTokenizer.class)
                .findAny()
                .map(ServiceLoader.Provider::get)
                .orElseGet(SimpleTokenizer::new);
    }

}
