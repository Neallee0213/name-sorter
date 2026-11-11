package com.neallee.namesorter.io;

import com.neallee.namesorter.domain.Name;

import java.util.List;

/** Somewhere names can be read from, such as a file. */
public interface NameSource {

    List<Name> readNames();
}
