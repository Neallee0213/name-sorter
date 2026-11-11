package com.neallee.namesorter.io;

import com.neallee.namesorter.domain.Name;

import java.util.List;

/** Somewhere names can be written to, such as the screen or a file. */
public interface NameOutput {

    void write(List<Name> names);
}
