package com.neallee.namesorter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** End-to-end tests: real files in, screen output and a real file out. */
class MainTest {

    private static final List<String> UNSORTED_EXAMPLE = List.of(
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

    private static final List<String> SORTED_EXAMPLE = List.of(
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

    @TempDir
    Path workingDirectory;

    private ByteArrayOutputStream screen;
    private ByteArrayOutputStream errors;

    @BeforeEach
    void captureOutput() {
        screen = new ByteArrayOutputStream();
        errors = new ByteArrayOutputStream();
    }

    @Test
    void printsSortedNamesAndWritesThemToSortedNamesListFile() throws IOException {
        Path input = Files.write(workingDirectory.resolve("unsorted-names-list.txt"), UNSORTED_EXAMPLE);

        int exitCode = run(input.toString());

        assertEquals(Main.EXIT_SUCCESS, exitCode);
        assertEquals(SORTED_EXAMPLE, screenLines());
        assertEquals(SORTED_EXAMPLE, Files.readAllLines(workingDirectory.resolve("sorted-names-list.txt")));
    }

    @Test
    void writesOutputToWorkingDirectoryNotNextToTheInputFile() throws IOException {
        Path inputFolder = Files.createDirectory(workingDirectory.resolve("input"));
        Path input = Files.write(inputFolder.resolve("names.txt"), UNSORTED_EXAMPLE);

        run(input.toString());

        assertTrue(Files.exists(workingDirectory.resolve("sorted-names-list.txt")));
        assertFalse(Files.exists(inputFolder.resolve("sorted-names-list.txt")));
    }

    @Test
    void showsUsageWhenNoFileIsGiven() {
        int exitCode = run();

        assertEquals(Main.EXIT_USAGE_ERROR, exitCode);
        assertTrue(errorText().contains("Usage"));
    }

    @Test
    void reportsMissingInputFile() {
        int exitCode = run(workingDirectory.resolve("does-not-exist.txt").toString());

        assertEquals(Main.EXIT_INPUT_ERROR, exitCode);
        assertTrue(errorText().contains("File not found"));
    }

    @Test
    void reportsInvalidNameWithoutWritingOutputFile() throws IOException {
        Path input = Files.write(workingDirectory.resolve("names.txt"), List.of("Leo Gardner", "Madonna"));

        int exitCode = run(input.toString());

        assertEquals(Main.EXIT_INPUT_ERROR, exitCode);
        assertTrue(errorText().contains("Line 2"));
        assertFalse(Files.exists(workingDirectory.resolve("sorted-names-list.txt")));
    }

    private int run(String... args) {
        return Main.run(args,
                new PrintStream(screen, true, StandardCharsets.UTF_8),
                new PrintStream(errors, true, StandardCharsets.UTF_8),
                workingDirectory);
    }

    private List<String> screenLines() {
        return screen.toString(StandardCharsets.UTF_8).lines().toList();
    }

    private String errorText() {
        return errors.toString(StandardCharsets.UTF_8);
    }
}
