package com.neallee.namesorter.io;

import com.neallee.namesorter.domain.Name;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

/** Prints names to the screen, one per line. */
public class ConsoleNameOutput implements NameOutput {

    private final PrintStream out;

    public ConsoleNameOutput(PrintStream out) {
        this.out = Objects.requireNonNull(out, "out must not be null");
    }

    @Override
    public void write(List<Name> names) {
        names.forEach(name -> out.println(name.fullName()));
    }
}
