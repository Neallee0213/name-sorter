package com.neallee.namesorter.domain;

import java.util.Comparator;
import java.util.List;

/**
 * Orders names by last name, then by given names, one given name at a time.
 *
 * <ul>
 *   <li>Comparison ignores case, so "adams" and "Adams" sort together.</li>
 *   <li>If one person's given names are a prefix of another's, the shorter
 *       one comes first: "Leo Gardner" before "Leo Ann Gardner".</li>
 *   <li>Names that differ only by case are finally ordered case-sensitively,
 *       so the result is always the same for the same input.</li>
 * </ul>
 */
public class LastNameThenGivenNamesComparator implements Comparator<Name> {

    private static final Comparator<String> IGNORING_CASE = String.CASE_INSENSITIVE_ORDER;

    @Override
    public int compare(Name first, Name second) {
        int byLastName = IGNORING_CASE.compare(first.lastName(), second.lastName());
        if (byLastName != 0) {
            return byLastName;
        }

        int byGivenNames = compareGivenNames(first.givenNames(), second.givenNames());
        if (byGivenNames != 0) {
            return byGivenNames;
        }

        return first.fullName().compareTo(second.fullName());
    }

    private int compareGivenNames(List<String> first, List<String> second) {
        int shared = Math.min(first.size(), second.size());

        for (int i = 0; i < shared; i++) {
            int result = IGNORING_CASE.compare(first.get(i), second.get(i));
            if (result != 0) {
                return result;
            }
        }

        return Integer.compare(first.size(), second.size());
    }
}
