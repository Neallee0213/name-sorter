package com.neallee.namesorter;

import com.neallee.namesorter.domain.Name;
import com.neallee.namesorter.domain.NameSorter;
import com.neallee.namesorter.io.NameOutput;
import com.neallee.namesorter.io.NameSource;

import java.util.List;
import java.util.Objects;

/**
 * The whole program in three steps: read names, sort them, write them out.
 *
 * <p>It only knows about abstractions, so tests can swap in fakes and
 * new inputs or outputs can be added without touching this class.
 */
public class NameSorterApp {

    private final NameSource source;
    private final NameSorter sorter;
    private final List<NameOutput> outputs;

    public NameSorterApp(NameSource source, NameSorter sorter, List<NameOutput> outputs) {
        this.source = Objects.requireNonNull(source, "source must not be null");
        this.sorter = Objects.requireNonNull(sorter, "sorter must not be null");
        this.outputs = List.copyOf(outputs);
    }

    public void run() {
        List<Name> names = source.readNames();
        List<Name> sortedNames = sorter.sort(names);
        outputs.forEach(output -> output.write(sortedNames));
    }
}
