package com.neallee.namesorter;

import com.neallee.namesorter.domain.InvalidNameException;
import com.neallee.namesorter.domain.LastNameThenGivenNamesComparator;
import com.neallee.namesorter.domain.NameParser;
import com.neallee.namesorter.domain.NameSorter;
import com.neallee.namesorter.io.ConsoleNameOutput;
import com.neallee.namesorter.io.FileNameOutput;
import com.neallee.namesorter.io.FileNameSource;

import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Command-line entry point: {@code name-sorter ./unsorted-names-list.txt}
 *
 * <p>This is the only place where concrete classes are chosen and wired
 * together. Everything else depends on interfaces.
 */
public final class Main {

    static final String OUTPUT_FILE_NAME = "sorted-names-list.txt";

    static final int EXIT_SUCCESS = 0;
    static final int EXIT_USAGE_ERROR = 1;
    static final int EXIT_INPUT_ERROR = 2;

    private Main() {
    }

    public static void main(String[] args) {
        Path workingDirectory = Path.of("").toAbsolutePath();
        System.exit(run(args, System.out, System.err, workingDirectory));
    }

    /** Runs the program and returns an exit code. Kept separate from {@link #main} so it can be tested. */
    static int run(String[] args, PrintStream out, PrintStream err, Path workingDirectory) {
        if (args.length != 1) {
            err.println("Usage: name-sorter <path-to-names-file>");
            return EXIT_USAGE_ERROR;
        }

        Path inputFile = Path.of(args[0]);
        if (!Files.isRegularFile(inputFile)) {
            err.println("File not found: " + inputFile);
            return EXIT_INPUT_ERROR;
        }

        NameSorterApp app = new NameSorterApp(
                new FileNameSource(inputFile, new NameParser()),
                new NameSorter(new LastNameThenGivenNamesComparator()),
                List.of(
                        new ConsoleNameOutput(out),
                        new FileNameOutput(workingDirectory.resolve(OUTPUT_FILE_NAME))));

        try {
            app.run();
            return EXIT_SUCCESS;
        } catch (InvalidNameException | UncheckedIOException e) {
            err.println("Error: " + e.getMessage());
            return EXIT_INPUT_ERROR;
        }
    }
}
