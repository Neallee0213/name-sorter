package com.neallee.namesorter.io;

import com.neallee.namesorter.domain.InvalidNameException;
import com.neallee.namesorter.domain.Name;
import com.neallee.namesorter.domain.NameParser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reads names from a text file, one name per line.
 *
 * <p>Blank lines are skipped. An invalid line stops the read with an error
 * that includes the line number, so the problem is easy to find.
 */
public class FileNameSource implements NameSource {

    private final Path file;
    private final NameParser parser;

    public FileNameSource(Path file, NameParser parser) {
        this.file = Objects.requireNonNull(file, "file must not be null");
        this.parser = Objects.requireNonNull(parser, "parser must not be null");
    }

    @Override
    public List<Name> readNames() {
        List<String> lines = readAllLines();
        List<Name> names = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            names.add(parseLine(line, i + 1));
        }

        return names;
    }

    private List<String> readAllLines() {
        try {
            return Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read file: " + file, e);
        }
    }

    private Name parseLine(String line, int lineNumber) {
        try {
            return parser.parse(line);
        } catch (InvalidNameException e) {
            throw new InvalidNameException("Line " + lineNumber + ": " + e.getMessage());
        }
    }
}
