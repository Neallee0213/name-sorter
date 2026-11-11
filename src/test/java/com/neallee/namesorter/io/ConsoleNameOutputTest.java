package com.neallee.namesorter.io;

import com.neallee.namesorter.domain.NameParser;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleNameOutputTest {

    @Test
    void printsOneNamePerLine() {
        ByteArrayOutputStream screen = new ByteArrayOutputStream();
        NameParser parser = new NameParser();

        new ConsoleNameOutput(new PrintStream(screen, true, StandardCharsets.UTF_8))
                .write(List.of(parser.parse("Marin Alvarez"), parser.parse("Adonis Julius Archer")));

        assertEquals(List.of("Marin Alvarez", "Adonis Julius Archer"),
                screen.toString(StandardCharsets.UTF_8).lines().toList());
    }
}
