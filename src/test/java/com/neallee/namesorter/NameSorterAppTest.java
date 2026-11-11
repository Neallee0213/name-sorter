package com.neallee.namesorter;

import com.neallee.namesorter.domain.LastNameThenGivenNamesComparator;
import com.neallee.namesorter.domain.Name;
import com.neallee.namesorter.domain.NameParser;
import com.neallee.namesorter.domain.NameSorter;
import com.neallee.namesorter.io.NameOutput;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Checks the read → sort → write flow using in-memory fakes, with no files involved. */
class NameSorterAppTest {

    private final NameParser parser = new NameParser();

    @Test
    void sendsTheSortedNamesToEveryOutput() {
        RecordingOutput screen = new RecordingOutput();
        RecordingOutput file = new RecordingOutput();

        NameSorterApp app = new NameSorterApp(
                () -> names("Leo Gardner", "Marin Alvarez"),
                new NameSorter(new LastNameThenGivenNamesComparator()),
                List.of(screen, file));

        app.run();

        List<Name> expected = names("Marin Alvarez", "Leo Gardner");
        assertEquals(expected, screen.received);
        assertEquals(expected, file.received);
    }

    private List<Name> names(String... fullNames) {
        return Stream.of(fullNames).map(parser::parse).toList();
    }

    /** A fake output that just remembers what it was given. */
    private static class RecordingOutput implements NameOutput {
        private final List<Name> received = new ArrayList<>();

        @Override
        public void write(List<Name> names) {
            received.addAll(names);
        }
    }
}
