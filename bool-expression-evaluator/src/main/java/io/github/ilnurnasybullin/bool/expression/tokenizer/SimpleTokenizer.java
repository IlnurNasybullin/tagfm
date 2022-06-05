package io.github.ilnurnasybullin.bool.expression.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SimpleTokenizer implements ExpressionTokenizer {

    private final static Pattern splitter = Pattern.compile("\s+");

    @Override
    public List<String> tokenize(String expression) {
        String[] tokens = splitter.split(expression.strip());
        return new ArrayList<>(Arrays.asList(tokens));
    }
}
