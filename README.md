# MarketValueTracker

## What problems does your application solve?

Following football transfers across many leagues means dealing with clubs,
squads and player values that are scattered and hard to compare. This
application organises that information into one searchable hierarchy of
competitions, from a top-level grouping down to individual leagues, so a
user can browse everything in one place. It answers practical questions
quickly: what is the combined market value of a league, which player is
currently the most valuable, who plays in a given position, and which club
a specific player currently plays for. It also supports moving a player between clubs
through a transfer, with checks so a transfer cannot happen if the buying
club cannot afford the fee or the player is not actually on the selling
club's books. All data can be saved to a file and loaded back later, so a
session's changes are not lost when the program closes.

## A description of the structure of your program

The data model is a tree of `Competition` objects, where each competition
holds a list of `Club` objects and a list of smaller sub-competitions
(for example, World contains Europe, which contains England, which
contains the Premier League). This makes `Competition` a recursive
structure, and its methods such as `totalMarketValue`, `findClub` and
`searchPlayersByName` walk the whole tree recursively. Each `Club` holds a
budget and a list of `Player` objects. Each `Player` has a birth date and a
market value, represented by dedicated `Date` and `Money` classes instead
of raw strings or numbers, so dates and amounts can be validated, compared
and formatted consistently. `FileManager` is the only class that reads or
writes files, saving and loading the whole hierarchy as a CSV file.
`TransferMarket` validates and performs transfers between clubs. `Client`
is the driver class: it loads the data, falls back to a small built-in
hierarchy if the file is missing, and runs a text menu that exercises every
feature. A set of JUnit test classes cover the non-trivial logic in `Date`,
`Money`, `Club`, `Competition`, `FileManager` and `TransferMarket` with a
range of normal and edge-case inputs.

## Clear instructions on how to run your application

- Open a terminal in the project's root folder (the folder containing `src`, `lib` and `data`).
- Create the output folder (skip if it already exists): `mkdir bin`
- Compile the project:
  `javac -cp "lib/junit-platform-console-standalone-1.7.2.jar" -d bin src/*.java`
- Run the program:
  `java -cp bin Client`
- Run the unit tests:
  `java -jar lib/junit-platform-console-standalone-1.7.2.jar -cp bin --scan-classpath`
- Once running, type a menu number and press Enter to choose an action,
  including viewing a single club's squad.
- The program automatically loads `data/football.csv`; use the save and
  load options in the menu to persist or reload changes made during the
  session.
