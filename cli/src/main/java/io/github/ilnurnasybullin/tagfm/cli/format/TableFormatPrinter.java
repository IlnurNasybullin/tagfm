package io.github.ilnurnasybullin.tagfm.cli.format;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class TableFormatPrinter {

    private final String formatPattern;
    private final String splitter;

    public TableFormatPrinter(String formatPattern, int rowLength) {
        this.formatPattern = formatPattern;
        splitter = String.valueOf('-').repeat(rowLength);
    }

    public void print(Object[] headers, Object[][] content) {
        System.out.printf(formatPattern, headers);
        System.out.println(splitter);
        for (Object[] row: content) {
            System.out.printf(formatPattern, row);
        }
    }

    public <T> void print(Object[] headers, Stream<T> content, List<Function<T, Object>> detail) {
        System.out.printf(formatPattern, headers);
        System.out.println(splitter);
        content.map(object -> detail.stream()
                        .map(function -> function.apply(object))
                        .toArray())
                .forEach(row -> System.out.printf(formatPattern, row));
    }
}
