package com.neallee.namesorter.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LastNameThenGivenNamesComparatorTest {

    private final LastNameThenGivenNamesComparator comparator = new LastNameThenGivenNamesComparator();
    private final NameParser parser = new NameParser();

    @Test
    void ordersByLastNameFirst() {
        assertComesBefore("Zoe Alvarez", "Adam Bentley");
    }

    @Test
    void ordersByFirstGivenNameWhenLastNamesMatch() {
        assertComesBefore("Adam Smith", "Brian Smith");
    }

    @Test
    void ordersByLaterGivenNamesWhenEarlierOnesMatch() {
        assertComesBefore("John Adam Smith", "John Brian Smith");
    }

    @Test
    void putsFewerGivenNamesFirstWhenTheyAreAPrefix() {
        assertComesBefore("Leo Gardner", "Leo Ann Gardner");
    }

    @Test
    void ignoresCaseWhenComparing() {
        assertComesBefore("bob adams", "Alice Baker");
        assertComesBefore("alice Smith", "Bob Smith");
    }

    @Test
    void ordersNamesThatDifferOnlyByCaseConsistently() {
        assertComesBefore("Leo Gardner", "leo gardner");
    }

    @Test
    void treatsIdenticalNamesAsEqual() {
        assertEquals(0, comparator.compare(parser.parse("Leo Gardner"), parser.parse("Leo Gardner")));
    }

    private void assertComesBefore(String earlier, String later) {
        Name first = parser.parse(earlier);
        Name second = parser.parse(later);

        assertTrue(comparator.compare(first, second) < 0, earlier + " should come before " + later);
        assertTrue(comparator.compare(second, first) > 0, later + " should come after " + earlier);
    }
}
