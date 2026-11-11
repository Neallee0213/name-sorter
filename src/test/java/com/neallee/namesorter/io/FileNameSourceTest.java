package com.neallee.namesorter.io;

import com.neallee.namesorter.domain.InvalidNameException;
import com.neallee.namesorter.domain.Name;
import com.neallee.namesorter.domain.NameParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileNameSourceTest {

    @TempDir
    Path tempDir;

    @Test
    void readsOneNamePerLineInFileOrder() throws IOException {
        Path file = writeFile("Janet Parsons\nAdonis Julius Archer\n");

        List<Name> names = sourceFor(file).readNames();

        assertEquals(List.of("Janet Parsons", "Adonis Julius Archer"), fullNames(names));
    }

    @Test
    void skipsBlankLines() throws IOException {
        Path file = writeFile("\nJanet Parsons\n   \n\nLeo Gardner\n");

        List<Name> names = sourceFor(file).readNames();

        assertEquals(List.of("Janet Parsons", "Leo Gardner"), fullNames(names));
    }

    @Test
    void handlesWindowsLineEndings() throws IOException {
        Path file = writeFile("Janet Parsons\r\nLeo Gardner\r\n");

        List<Name> names = sourceFor(file).readNames();

        assertEquals(List.of("Janet Parsons", "Leo Gardner"), fullNames(names));
    }

    @Test
    void returnsNoNamesForEmptyFile() throws IOException {
        Path file = writeFile("");

        assertTrue(sourceFor(file).readNames().isEmpty());
    }

    @Test
    void reportsTheLineNumberOfAnInvalidName() throws IOException {
        Path file = writeFile("Janet Parsons\n\nMadonna\n");

        InvalidNameException error = assertThrows(InvalidNameException.class, () -> sourceFor(file).readNames());

        assertTrue(error.getMessage().startsWith("Line 3:"), error.getMessage());
    }

    @Test
    void failsClearlyWhenFileDoesNotExist() {
        Path missing = tempDir.resolve("missing.txt");

        assertThrows(UncheckedIOException.class, () -> sourceFor(missing).readNames());
    }

    private FileNameSource sourceFor(Path file) {
        return new FileNameSource(file, new NameParser());
    }

    private Path writeFile(String content) throws IOException {
        return Files.writeString(tempDir.resolve("names.txt"), content);
    }

    private List<String> fullNames(List<Name> names) {
        return names.stream().map(Name::fullName).toList();
    }
}
