package com.neallee.namesorter.io;

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

class FileNameOutputTest {

    @TempDir
    Path tempDir;

    private final NameParser parser = new NameParser();

    @Test
    void writesOneNamePerLine() throws IOException {
        Path file = tempDir.resolve("sorted.txt");

        new FileNameOutput(file).write(List.of(parser.parse("Marin Alvarez"), parser.parse("Leo Gardner")));

        assertEquals(List.of("Marin Alvarez", "Leo Gardner"), Files.readAllLines(file));
    }

    @Test
    void overwritesAnExistingFile() throws IOException {
        Path file = Files.writeString(tempDir.resolve("sorted.txt"), "Old Content\nThat Should\nBe Gone\n");

        new FileNameOutput(file).write(List.of(parser.parse("Leo Gardner")));

        assertEquals(List.of("Leo Gardner"), Files.readAllLines(file));
    }

    @Test
    void writesAnEmptyFileWhenThereAreNoNames() throws IOException {
        Path file = tempDir.resolve("sorted.txt");

        new FileNameOutput(file).write(List.<Name>of());

        assertEquals(List.of(), Files.readAllLines(file));
    }

    @Test
    void failsClearlyWhenFileCannotBeWritten() throws IOException {
        Path folderNotFile = Files.createDirectory(tempDir.resolve("sorted.txt"));

        assertThrows(UncheckedIOException.class,
                () -> new FileNameOutput(folderNotFile).write(List.of(parser.parse("Leo Gardner"))));
    }
}
