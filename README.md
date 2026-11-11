# Name Sorter

![Build and test](https://github.com/Neallee0213/name-sorter/actions/workflows/build.yml/badge.svg)

Sorts a list of names by **last name**, then by **given names**.
Prints the result to the screen and writes it to `sorted-names-list.txt` in the current folder.

## Quick start

Requires **Java 17+** and **Maven 3.8+**.

```sh
mvn package                                  # build and run all tests
./name-sorter ./unsorted-names-list.txt      # Linux / macOS
name-sorter .\unsorted-names-list.txt        # Windows
```

Or run the jar directly: `java -jar target/name-sorter.jar ./unsorted-names-list.txt`

Run tests only: `mvn test`

## Input format

One name per line. Each name has 1 to 3 given names followed by a last name:

```
Janet Parsons
Adonis Julius Archer
Hunter Uriah Mathew Clarke
```

## How the code is organised

The program does three things: **read → sort → write**. Each step has its own class.

| Class | Job |
|---|---|
| `Main` | Entry point. Checks arguments and wires the pieces together. |
| `NameSorterApp` | Runs read → sort → write. Depends only on interfaces. |
| `domain/Name` | A valid name: 1–3 given names and a last name. |
| `domain/NameParser` | Turns `"Adonis Julius Archer"` into a `Name`. |
| `domain/LastNameThenGivenNamesComparator` | The sorting rule. |
| `domain/NameSorter` | Sorts a list using whichever rule it is given. No file or screen access. |
| `io/NameSource`, `io/FileNameSource` | Where names come from. |
| `io/NameOutput`, `io/ConsoleNameOutput`, `io/FileNameOutput` | Where sorted names go. |

To change the sorting rule, pass a different `Comparator<Name>` to `NameSorter`.
To add a new output (e.g. CSV), implement `NameOutput` and add it in `Main`. Nothing else needs to change.

## Sorting rules and decisions

- **Last name first**, then given names **one at a time** (first given name, then second, and so on).
- If one person's given names are the start of another's, the **shorter goes first**: `Leo Gardner` before `Leo Ann Gardner`.
- Comparison **ignores case**. Names that differ only by case are then ordered consistently, so the output never changes between runs.
- **Duplicates are kept.** Two people can share a name.
- **Blank lines are skipped.** Extra spaces and Windows line endings are fine.
- **Invalid names stop the program** with the line number (e.g. `Line 3: ... must have between 1 and 3 given names`). Nothing is written, so a bad input never produces a half-correct output file.
- **Plain alphabetical comparison.** Accented letters (e.g. `É`) sort after `Z`. Locale-aware sorting (`java.text.Collator`) could be swapped in through `NameSorter` if needed.

## Exit codes

| Code | Meaning |
|---|---|
| 0 | Success |
| 1 | Wrong arguments |
| 2 | Input file missing, unreadable, or contains an invalid name |

## Continuous integration

GitHub Actions builds the project, runs all tests, and runs the program against the sample file on every push and pull request. See `.github/workflows/build.yml`.
