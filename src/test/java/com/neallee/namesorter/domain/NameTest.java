package com.neallee.namesorter.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NameTest {

    @Test
    void fullNameJoinsGivenNamesAndLastNameWithSpaces() {
        Name name = new Name(List.of("Adonis", "Julius"), "Archer");

        assertEquals("Adonis Julius Archer", name.fullName());
    }

    @Test
    void rejectsNameWithNoGivenNames() {
        assertThrows(InvalidNameException.class, () -> new Name(List.of(), "Archer"));
    }

    @Test
    void rejectsNameWithMoreThanThreeGivenNames() {
        List<String> fourGivenNames = List.of("A", "B", "C", "D");

        assertThrows(InvalidNameException.class, () -> new Name(fourGivenNames, "Archer"));
    }

    @Test
    void rejectsBlankLastName() {
        assertThrows(InvalidNameException.class, () -> new Name(List.of("Adonis"), " "));
    }

    @Test
    void givenNamesCannotBeChangedAfterCreation() {
        List<String> givenNames = new ArrayList<>(List.of("Adonis"));
        Name name = new Name(givenNames, "Archer");

        givenNames.add("Julius");

        assertEquals(List.of("Adonis"), name.givenNames());
    }

    @Test
    void rejectsBlankGivenName() {
        assertThrows(InvalidNameException.class, () -> new Name(List.of(" "), "Archer"));
    }

    @Test
    void rejectsMissingLastName() {
        assertThrows(InvalidNameException.class, () -> new Name(List.of("Adonis"), null));
    }
}
