package com.neallee.namesorter.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NameParserTest {

    private final NameParser parser = new NameParser();

    @Test
    void treatsLastWordAsLastNameAndTheRestAsGivenNames() {
        Name name = parser.parse("Hunter Uriah Mathew Clarke");

        assertEquals(List.of("Hunter", "Uriah", "Mathew"), name.givenNames());
        assertEquals("Clarke", name.lastName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Marin Alvarez", "Adonis Julius Archer", "Hunter Uriah Mathew Clarke"})
    void acceptsOneToThreeGivenNames(String text) {
        assertEquals(text, parser.parse(text).fullName());
    }

    @Test
    void ignoresExtraWhitespaceAroundAndBetweenWords() {
        Name name = parser.parse("  Adonis \t Julius   Archer \r");

        assertEquals("Adonis Julius Archer", name.fullName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Madonna", "Hunter Uriah Mathew Leo Clarke"})
    void rejectsWrongNumberOfGivenNames(String text) {
        InvalidNameException error = assertThrows(InvalidNameException.class, () -> parser.parse(text));

        assertTrue(error.getMessage().contains(text), "error should mention the bad name");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void rejectsEmptyText(String text) {
        assertThrows(InvalidNameException.class, () -> parser.parse(text));
    }
}
