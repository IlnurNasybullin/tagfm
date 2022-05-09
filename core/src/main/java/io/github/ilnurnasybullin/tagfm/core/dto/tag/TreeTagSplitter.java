package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import java.util.Arrays;
import java.util.regex.Pattern;

public class TreeTagSplitter {

    private final static Pattern splitter = Pattern.compile("/");

    public String[] tagNames(String fullName) {
        String[] tagNames = splitter.split(fullName);
        if (tagNames.length > 0 && tagNames[0].isEmpty()) {
            return Arrays.copyOfRange(tagNames, 1, tagNames.length);
        }

        return tagNames;
    }

}
