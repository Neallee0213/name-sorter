package com.neallee.namesorter.io;

import com.neallee.namesorter.domain.Name;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/** Writes names to a text file, one per line. An existing file is overwritten. */
public class FileNameOutput implements NameOutput {

    private final Path file;

    public FileNameOutput(Path file) {
        this.file = Objects.requireNonNull(file, "file must not be null");
    }

    @Override
    public void write(List<Name> names) {
        List<String> lines = names.stream().map(Name::fullName).toList();
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write file: " + file, e);
        }
    }
}
