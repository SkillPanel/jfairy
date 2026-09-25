# Weighted data values

## Goal

Let a data list say how often each value is picked, and use real frequencies where a source exists. `pl` first
names and last names come first, from the PESEL registry. Today every list is picked uniformly, and the `pl` data has
99 alphabetical last names per sex with neither Nowak nor Kowalski among them.

Repeating an element is not a weighting mechanism: #184 removes the accidental repeats and makes the build reject
them, so an explicit weight is the only way to express frequency.

## Non-goals

- Weights for other locales. The mechanism works for any list, but only `pl` gets real data now.
- Separate second-name statistics: `middleName` keeps drawing from `firstNames`.
- Weights in `DataMaster` implementations other than `MapBasedDataMaster`.

## 1. Format

- A list element is `value*weight`, e.g. `Nowak*98387`. An element without `*` has weight 1. Weights are relative
  within one list.
- `*` is used because no bundled data contains it and YAML treats it as an indicator only at the start of a scalar.
  `:` is ruled out: it is exactly what turned elements into maps (#177).
- Weights are raw counts from the source, not normalized, so a yearly refresh only swaps numbers. Sums are held in a
  `long`.
- The same syntax appears in YAML, in the generated `.properties` (`lastNames.male=Nowak*98387,Kowalski*66589`) and
  in users' custom `.properties` files.
- `build/yaml2properties.groovy` validates each element:
  - the weight is a positive integer;
  - the value before `*` passes the existing element rules (non-empty, no `,`, no surrounding or consecutive
    whitespace, no control character, no `;`, no trailing `:`);
  - the no-repeat rule compares values, so `Nowak*5, Nowak*3` fails;
  - `*` is reserved: an element with more than one `*` fails.
- Files without `*`, which is every current data file and every existing custom file, behave exactly as before.

## 2. Runtime

All changes are inside `MapBasedDataMaster`. The `DataMaster` interface is unchanged.

- `splitAll`, which already splits every value once in `readResources`, also separates weights. Each key maps to an
  internal immutable object holding the values and, for weighted lists, their cumulative weights. It is built once, so
  a created `Fairy` stays safe to share between threads.
- A weighted pick draws once with `baseProducer.randomBetween(1L, total)` and binary-searches the cumulative weights:
  one random draw per pick, as today.
- A list with no weighted element keeps calling `baseProducer.randomElement(values)`. Random sequences for every
  unweighted list are therefore unchanged, and seeded snapshots outside `pl` do not move.
- `getRandomValue` and `getValuesOfType` pick by weight. `getValuesOfType` converts the bare value, so `4*3` read as
  `Integer.class` gives `4`.
- `getStringList` returns bare values without weights. Its javadoc states that picking from that list yourself is
  uniform. `JaCompanyProvider`, its only direct caller, is unaffected.
- A malformed weight in a custom file (`Nowak*0`, `Nowak*x`, `A*1*2`) throws `IllegalArgumentException` naming the
  file and the key the first time that key is used as a list (`getStringList`, `getRandomValue`,
  `getValuesOfType`), whichever element is picked. It cannot fail while `Fairy` is being created: `.properties` does
  not tell a list from a scalar, and a scalar such as `text` may legitimately contain `*`. `getString` never fails
  on weights.

## 3. `pl` data from PESEL

Sources (Ministerstwo Cyfryzacji, CC0 1.0, refreshed yearly, each resource also offered as CSV):

- last names: https://dane.gov.pl/pl/dataset/1681 ("Nazwiska męskie" / "Nazwiska żeńskie")
- first names: https://dane.gov.pl/pl/dataset/1667 ("lista imion męskich/żeńskich ... imię pierwsze")

Observed in the 2026-01-20 data:

- Values are upper case (`NOWAK`, `WIŚNIEWSKI`).
- Male last names: 408 thousand distinct names, top 1000 cover 30.6% of people, and the 1000th name has about 2500
  people.
- Male first names: top 500 cover 96.3%. The next 500 are mostly rare or foreign names with about 300 to 900 people.
- Junk rows exist, e.g. the first name `BRAK DANYCH`.

Design:

- `build/pesel2yaml.groovy`, JDK only, in the style of `yaml2properties.groovy`. It needs the network and runs about
  once a year, so it is not part of the normal build: a Maven profile runs it
  through the existing gmavenplus plugin, bound to `initialize` so the YAML is rewritten before `yaml2properties`
  runs: `./mvnw -Ppesel initialize`. The CSV URLs are parameters, and the script header documents the source
  datasets.
- Processing:
  - title-case with the `pl` locale, including after a hyphen or an apostrophe (`WIŚNIEWSKI` → `Wiśniewski`,
    `NOWAK-KOWALSKA` → `Nowak-Kowalska`);
  - keep only letters, optionally joined by a hyphen or an apostrophe. That drops junk such as `BRAK DANYCH`; no
    name within the limits contains a space;
  - sort by count descending and keep the top N.
- Limits: 1000 last names and 500 first names per sex.
- The script replaces only the `firstNames` and `lastNames` blocks of `jfairy_pl.yml` and leaves the rest of the file
  untouched. It writes block style, one value per line, so a yearly refresh gives a readable diff, headed by a comment
  naming the source, the data date, the licence and the regenerate command:

  ```yaml
  # PESEL registry (dane.gov.pl, CC0), state as of 2026-01-20; regenerate with ./mvnw -Ppesel initialize
  lastNames:
      male:
      - Nowak*98387
      - Kowalski*66589
  ```

## 4. Tests

- `Yaml2PropertiesScriptSpec`:
  - `Nowak*5` passes through to `.properties` unchanged;
  - failures: `A*0`, `A*x`, `A*1*2`, `*5`, `[A*5, A*3]`, and the existing element rules applied to the value part.
- `MapBasedDataMasterSpec`:
  - `getStringList` returns bare values;
  - weighted pick at interval boundaries with a stubbed random source: for `[A*1, B*3]`, 1 gives `A`, 2 and 4 give
    `B`;
  - distribution sanity: 10,000 seeded picks from `[A*1, B*9]` give `B` a share between 85% and 95%;
  - `getValuesOfType` with `Integer.class` on `4*3` returns `4`;
  - a custom file with `Nowak*0` fails on the first use of that key, naming the file and the key, while a custom
    `text` containing `*` stays readable.
- Unweighted lists: the seeded snapshots for `de`, `fr`, `ja` and `ka` pass unchanged. Only `pl` snapshots are
  updated.
- A spec for `pesel2yaml.groovy` on small CSV fixtures in a temporary directory, without network: title-casing
  (hyphen, Polish letters), junk filtering, sorting, the limit, block replacement that keeps the rest of the file, and
  the source comment.
- Existing specs: `BundledDataEquivalenceSpec` must keep passing with weighted values; `GeneratedValuesVarySpec`
  (#184) keeps checking that `pl` names vary.
- README, "Custom data": document the `*weight` syntax with an example.

## Delivery

- Branch `feat/weighted-data`, based on `test/data-quality-checks` (#184), because both change `joinList` in the
  build script and the no-repeat rule is part of this design. Rebase onto `master` once #184 is merged.
- Suggested commits: format and validation in the build script; weighted picking in `MapBasedDataMaster`; the
  `pesel2yaml` script and profile; regenerated `pl` data with updated snapshots; README.
