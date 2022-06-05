package io.github.ilnurnasybullin.bool.expression.tokenizer;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Function;

@FunctionalInterface
public interface ExpressionTokenizer extends Function<String, List<String>> {

    @Override
    default List<String> apply(String expression) {
        return tokenize(expression);
    }

    List<String> tokenize(String expression);

    static ExpressionTokenizer getInstance() {
        return ServiceLoader.load(ExpressionTokenizer.class)
                .stream()
                .filter(provider -> provider.type() != SimpleTokenizer.class)
                .findAny()
                .map(ServiceLoader.Provider::get)
                .orElseGet(SimpleTokenizer::new);
    }

}
