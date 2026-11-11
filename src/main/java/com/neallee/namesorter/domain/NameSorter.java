package com.neallee.namesorter.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Sorts names using a given ordering.
 *
 * <p>This class does no reading or writing, which keeps it simple to test.
 * The ordering is passed in, so a different rule (e.g. by first name)
 * can be used without changing this class.
 */
public class NameSorter {

    private final Comparator<Name> ordering;

    public NameSorter(Comparator<Name> ordering) {
        this.ordering = Objects.requireNonNull(ordering, "ordering must not be null");
    }

    /** Returns a new sorted list. The input list is left unchanged. */
    public List<Name> sort(List<Name> names) {
        Objects.requireNonNull(names, "names must not be null");
        return names.stream().sorted(ordering).toList();
    }
}
