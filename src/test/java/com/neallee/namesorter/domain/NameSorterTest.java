package com.neallee.namesorter.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NameSorterTest {

    private final NameParser parser = new NameParser();
    private final NameSorter sorter = new NameSorter(new LastNameThenGivenNamesComparator());

    @Test
    void sortsTheExampleFromTheAssessment() {
        List<Name> unsorted = names(
                "Janet Parsons",
                "Vaughn Lewis",
                "Adonis Julius Archer",
                "Shelby Nathan Yoder",
                "Marin Alvarez",
                "London Lindsey",
                "Beau Tristan Bentley",
                "Leo Gardner",
                "Hunter Uriah Mathew Clarke",
                "Mikayla Lopez",
                "Frankie Conner Ritter");

        List<Name> expected = names(
                "Marin Alvarez",
                "Adonis Julius Archer",
                "Beau Tristan Bentley",
                "Hunter Uriah Mathew Clarke",
                "Leo Gardner",
                "Vaughn Lewis",
                "London Lindsey",
                "Mikayla Lopez",
                "Janet Parsons",
                "Frankie Conner Ritter",
                "Shelby Nathan Yoder");

        assertEquals(expected, sorter.sort(unsorted));
    }

    @Test
    void keepsDuplicateNames() {
        List<Name> sorted = sorter.sort(names("Leo Gardner", "Marin Alvarez", "Leo Gardner"));

        assertEquals(names("Marin Alvarez", "Leo Gardner", "Leo Gardner"), sorted);
    }

    @Test
    void returnsEmptyListForEmptyInput() {
        assertTrue(sorter.sort(List.of()).isEmpty());
    }

    @Test
    void doesNotChangeTheInputList() {
        List<Name> input = new ArrayList<>(names("Leo Gardner", "Marin Alvarez"));
        List<Name> copyBeforeSorting = List.copyOf(input);

        sorter.sort(input);

        assertEquals(copyBeforeSorting, input);
    }

    @Test
    void usesWhateverOrderingItIsGiven() {
        NameSorter byFirstGivenName = new NameSorter(Comparator.comparing(name -> name.givenNames().get(0)));

        List<Name> sorted = byFirstGivenName.sort(names("Zoe Adams", "Amy Young"));

        assertEquals(names("Amy Young", "Zoe Adams"), sorted);
    }

    private List<Name> names(String... fullNames) {
        return Stream.of(fullNames).map(parser::parse).toList();
    }
}
