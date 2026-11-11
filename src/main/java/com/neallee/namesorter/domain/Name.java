package com.neallee.namesorter.domain;

import java.util.List;

/**
 * A person's name: one to three given names followed by a single last name.
 *
 * <p>Instances are immutable and always valid. Use {@link NameParser} to create
 * one from a line of text.
 */
public record Name(List<String> givenNames, String lastName) {

    public static final int MIN_GIVEN_NAMES = 1;
    public static final int MAX_GIVEN_NAMES = 3;

    public Name {
        if (lastName == null || lastName.isBlank()) {
            throw new InvalidNameException("A name must have a last name.");
        }
        if (givenNames == null
                || givenNames.size() < MIN_GIVEN_NAMES
                || givenNames.size() > MAX_GIVEN_NAMES) {
            int count = givenNames == null ? 0 : givenNames.size();
            throw new InvalidNameException(String.format(
                    "A name must have between %d and %d given names, but found %d.",
                    MIN_GIVEN_NAMES, MAX_GIVEN_NAMES, count));
        }
        if (givenNames.stream().anyMatch(name -> name == null || name.isBlank())) {
            throw new InvalidNameException("Given names must not be blank.");
        }

        givenNames = List.copyOf(givenNames);
    }

    /** Returns the name the way a person would write it, e.g. "Adonis Julius Archer". */
    public String fullName() {
        return String.join(" ", givenNames) + " " + lastName;
    }

    @Override
    public String toString() {
        return fullName();
    }
}
