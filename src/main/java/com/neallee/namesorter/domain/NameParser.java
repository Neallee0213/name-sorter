package com.neallee.namesorter.domain;

import java.util.Arrays;
import java.util.List;

/**
 * Turns a line of text such as {@code "Hunter Uriah Mathew Clarke"} into a {@link Name}.
 *
 * <p>The last word is the last name; every word before it is a given name.
 * Words may be separated by any amount of whitespace.
 */
public class NameParser {

    private static final String WHITESPACE = "\\s+";

    public Name parse(String text) {
        if (text == null || text.isBlank()) {
            throw new InvalidNameException("A name cannot be empty.");
        }

        List<String> words = Arrays.asList(text.strip().split(WHITESPACE));
        int lastIndex = words.size() - 1;

        try {
            return new Name(words.subList(0, lastIndex), words.get(lastIndex));
        } catch (InvalidNameException e) {
            throw new InvalidNameException("Invalid name \"" + text.strip() + "\": " + e.getMessage());
        }
    }
}
