# Weighted Data Values Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Let a data list element carry a weight (`Nowak*98387`) that makes it picked proportionally more often, and
regenerate the `pl` first and last names with real frequencies from the PESEL registry.

**Architecture:** The build script `build/yaml2properties.groovy` validates the `value*weight` syntax and copies it
unchanged into the generated `.properties`. At runtime a new package-private `WeightedList` holds one parsed list
(values, raw elements, cumulative weights) and does the pick; `MapBasedDataMaster` stores one `WeightedList` per key
instead of a `List<String>`. A separate, manually run script `build/pesel2yaml.groovy` (Maven profile `pesel`)
rewrites the name blocks of `jfairy_pl.yml` from the PESEL CSV files.

**Tech Stack:** Java 17, Groovy scripts run by gmavenplus, Spock specs, Maven wrapper (`./mvnw`).

**Spec:** `docs/superpowers/specs/2026-09-25-weighted-data-design.md`

## Global Constraints

- Java 17 (`<javaVersion>17</javaVersion>`); no new runtime dependency. Build scripts use the JDK and Groovy only.
- The `DataMaster` interface keeps its methods and signatures; only javadoc changes.
- A list without any `*` must be picked through `baseProducer.randomElement(values)`, exactly as today, so seeded
  results of every unweighted list stay identical.
- Weights are positive integers, relative within one list; an element without `*` weighs 1. Sums are `long`.
- `getStringList` returns bare values without weights.
- `./mvnw verify` must stay green, including Checkstyle, PMD and Error Prone. Remove imports that become unused.
- Everything written into the repo is English. Commits follow Conventional Commits, with no AI attribution and no
  ticket number.
- Branch `feat/weighted-data`, based on `test/data-quality-checks` (#184). Do not rebase it.

## Review Focus

- A custom `.properties` scalar such as `text=Hello *world*` must stay readable through `getString`: weights are
  parsed for every key, so a parse failure must not surface until the key is used as a list (test in Task 3).
- A custom typed key with weights (`firstNames.male=Homer*3,Bart`) replaces only its own type and keeps the other
  bundled types (test in Task 3).
- Whitespace around the separator in a hand-written custom file (`Nowak * 5`) is tolerated like the existing
  whitespace around commas (test in Task 2).
- Weights that add up past `Long.MAX_VALUE` fail with `IllegalArgumentException`, not an `ArithmeticException` or a
  wrong pick (test in Task 2).
- A PESEL URL that returns no usable rows (a moved resource, an HTML error page) must fail the script instead of
  writing empty name blocks (test in Task 4).

---

### Task 1: Weight syntax in the build script

**Files:**
- Modify: `build/yaml2properties.groovy` (header comment, `joinList`, new `valueOf`)
- Test: `src/test/groovy/com/devskiller/jfairy/data/Yaml2PropertiesScriptSpec.groovy`

**Interfaces:**
- Consumes: nothing from other tasks. `checkElementText(String file, String key, String text)` and
  `check(boolean, String file, String key, String message)` already exist in the script.
- Produces: `.properties` list values whose elements may be `value*weight`, copied verbatim from YAML. Tasks 3 and 5
  rely on that.

- [ ] **Step 1: Write the failing tests**

In `Yaml2PropertiesScriptSpec.groovy`, add this feature method after `"writes sorted keys without a timestamp comment"`:

```groovy
    def "passes weighted elements through unchanged"() {
        when:
            Properties properties = convert('jfairy_xx.yml', 'lastNames: {male: [Nowak*98387, Kowalski*66589, Adamiec]}\n')

        then:
            properties == ['lastNames.male': 'Nowak*98387,Kowalski*66589,Adamiec']
    }
```

and append these rows to the `where:` table of `"fails on #problem"` (after the `'a trailing colon'` row):

```groovy
            'a zero weight'                   | 'cities: [A*0]'                       | 'weight must be a positive integer: [A*0]'
            'a negative weight'               | "cities: ['A*-1']"                    | 'weight must be a positive integer'
            'a non-numeric weight'            | 'cities: [A*x]'                       | 'weight must be a positive integer'
            'two weights'                     | 'cities: [A*1*2]'                     | 'weight must be a positive integer'
            'a weight without a value'        | "cities: ['*5']"                      | 'must be non-empty'
            'a value repeated with weights'   | 'cities: [A*5, A*3]'                  | 'repeats [A]'
            'whitespace before the weight'    | "cities: ['A *5']"                    | 'surrounding whitespace'
```

`'*5'` must be quoted: an unquoted leading `*` is a YAML alias.

- [ ] **Step 2: Run the spec to verify the new cases fail**

Run: `./mvnw -q test -Dtest=Yaml2PropertiesScriptSpec`
Expected: FAIL. `a zero weight`, `a negative weight`, `a non-numeric weight`, `two weights` and
`a value repeated with weights` report "no exception was thrown"; `a weight without a value` and
`whitespace before the weight` may already pass or fail on a different message. The pass-through test passes already
(the script copies text verbatim); it pins the behavior for later.

- [ ] **Step 3: Implement the weight syntax**

In `build/yaml2properties.groovy`, extend the output format in the header comment:

```groovy
 * Output format:
 *   scalar        key=value
 *   list          key=a,b,c
 *   map of lists  key.subKey=a,b,c
 * A list element may carry a weight, value*weight, which is copied unchanged.
 * Anything else fails the build, naming the file and key. Keys are sorted and no timestamp is written,
 * so the output is reproducible.
```

Replace `joinList` with:

```groovy
String joinList(String file, String key, List values) {
    check(!values.isEmpty(), file, key, 'list must not be empty')
    Set<String> seen = [] as Set
    values.collect { element ->
        String text = scalar(file, key, element)
        check(!text.contains(',') && !text.contains('\n'), file, key,
            "list element must contain neither ',' nor a newline: [${text}]")
        String value = valueOf(file, key, text)
        check(!value.isEmpty(), file, key, "list element must be non-empty: [${text}]")
        checkElementText(file, key, value)
        check(seen.add(value), file, key, "list element repeats [${value}]")
        text
    }.join(',')
}

// An element is 'value' or 'value*weight'; the weight is a positive integer that fits in a long
String valueOf(String file, String key, String text) {
    int separator = text.indexOf('*')
    if (separator < 0) {
        return text
    }
    check(text.substring(separator + 1) ==~ /[1-9][0-9]{0,17}/, file, key,
        "list element weight must be a positive integer: [${text}]")
    text.substring(0, separator)
}
```

The existing rows `'a comma inside an element'` (expects `"','"`) and `'an empty element'` (expects `'non-empty'`)
still match the new messages.

- [ ] **Step 4: Run the spec to verify it passes**

Run: `./mvnw -q test -Dtest=Yaml2PropertiesScriptSpec`
Expected: PASS, 29 iterations and features, 0 failures.

- [ ] **Step 5: Commit**

```bash
git add build/yaml2properties.groovy src/test/groovy/com/devskiller/jfairy/data/Yaml2PropertiesScriptSpec.groovy
git commit -m "build: accept weighted list elements in data YAML"
```

---

### Task 2: `WeightedList`

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/data/WeightedList.java`
- Test: `src/test/groovy/com/devskiller/jfairy/data/WeightedListSpec.groovy`

**Interfaces:**
- Consumes: `BaseProducer.randomElement(List<T>)` and `BaseProducer.randomBetween(long min, long max)` (both bounds
  inclusive).
- Produces (package-private, used by Task 3):
  - `static WeightedList parse(String raw)` – throws `IllegalArgumentException` on a malformed weight;
  - `List<String> values()` – bare values, unmodifiable;
  - `List<String> elements()` – trimmed elements as written, weights included, unmodifiable;
  - `boolean isEmpty()`;
  - `String pick(BaseProducer baseProducer)`.

- [ ] **Step 1: Write the failing spec**

Create `src/test/groovy/com/devskiller/jfairy/data/WeightedListSpec.groovy`:

```groovy
package com.devskiller.jfairy.data

import spock.lang.Specification

import com.devskiller.jfairy.producer.BaseProducer

class WeightedListSpec extends Specification {

    def "splits on commas, trims and skips empty elements"() {
        when:
            WeightedList list = WeightedList.parse('A, B ,, ')

        then:
            list.values() == ['A', 'B']
            list.elements() == ['A', 'B']
            !list.isEmpty()
    }

    def "separates values from weights"() {
        when:
            WeightedList list = WeightedList.parse('Nowak*98387, Adamiec')

        then:
            list.values() == ['Nowak', 'Adamiec']
            list.elements() == ['Nowak*98387', 'Adamiec']
    }

    def "tolerates whitespace around the weight separator"() {
        expect:
            WeightedList.parse('Nowak * 5').values() == ['Nowak']
    }

    def "an empty value is an empty list"() {
        expect:
            WeightedList.parse(' , ').isEmpty()
    }

    def "picks an unweighted list uniformly, as before weights existed"() {
        given:
            BaseProducer baseProducer = Mock()

        when:
            String picked = WeightedList.parse('A,B').pick(baseProducer)

        then:
            1 * baseProducer.randomElement(['A', 'B']) >> 'B'
            0 * baseProducer.randomBetween(_, _)
            picked == 'B'
    }

    def "picks the element whose weight interval contains the drawn number #drawn"() {
        given:
            BaseProducer baseProducer = Stub {
                randomBetween(1L, 4L) >> drawn
            }

        expect:
            WeightedList.parse('A*1,B*3').pick(baseProducer) == expected

        where:
            drawn | expected
            1L    | 'A'
            2L    | 'B'
            4L    | 'B'
    }

    def "an element without a weight weighs 1 in a weighted list"() {
        given:
            BaseProducer baseProducer = Stub {
                randomBetween(1L, 3L) >> 3L
            }

        expect:
            WeightedList.parse('A*2,B').pick(baseProducer) == 'B'
    }

    def "rejects the malformed element #element"() {
        when:
            WeightedList.parse("Adamiec,${element}")

        then:
            IllegalArgumentException ex = thrown()
            ex.message == message

        where:
            element    | message
            'Nowak*0'  | "Element 'Nowak*0' must have a positive integer weight"
            'Nowak*-1' | "Element 'Nowak*-1' must have a positive integer weight"
            'Nowak*x'  | "Element 'Nowak*x' must have a positive integer weight"
            'Nowak*'   | "Element 'Nowak*' must have a positive integer weight"
            'A*1*2'    | "Element 'A*1*2' must have a positive integer weight"
            '*5'       | "Element '*5' has a weight but no value"
    }

    def "rejects weights that add up past Long.MAX_VALUE"() {
        when:
            WeightedList.parse("A*${Long.MAX_VALUE},B*1")

        then:
            IllegalArgumentException ex = thrown()
            ex.message == "Weights add up to more than ${Long.MAX_VALUE}"
    }
}
```

- [ ] **Step 2: Run the spec to verify it fails**

Run: `./mvnw -q test -Dtest=WeightedListSpec`
Expected: FAIL at test compilation, `unable to resolve class WeightedList`.

- [ ] **Step 3: Implement `WeightedList`**

Create `src/main/java/com/devskiller/jfairy/data/WeightedList.java`:

```java
package com.devskiller.jfairy.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jspecify.annotations.Nullable;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.util.ValidateUtils;

/**
 * One data list, split once from its comma-separated value. An element is {@code value} or {@code value*weight},
 * where the weight is a positive integer relative to the other elements of the list and a missing weight counts as 1.
 * A list without any weight is picked uniformly, exactly as before weights existed.
 */
final class WeightedList {

    private static final String LIST_SEPARATOR = ",";
    private static final char WEIGHT_SEPARATOR = '*';

    private final List<String> elements;
    private final List<String> values;
    // Running totals of the weights, or null when no element has a weight
    private final long @Nullable [] cumulativeWeights;

    private WeightedList(List<String> elements, List<String> values, long @Nullable [] cumulativeWeights) {
        this.elements = elements;
        this.values = values;
        this.cumulativeWeights = cumulativeWeights;
    }

    /**
     * @throws IllegalArgumentException if a weight is not a positive integer, an element has a weight but no value,
     *                                  or the weights add up past {@link Long#MAX_VALUE}
     */
    static WeightedList parse(String raw) {
        List<String> elements = Arrays.stream(raw.split(LIST_SEPARATOR))
            .map(String::strip)
            .filter(element -> !element.isEmpty())
            .toList();
        List<String> values = new ArrayList<>(elements.size());
        long[] cumulativeWeights = new long[elements.size()];
        boolean weighted = false;
        long total = 0;
        for (int i = 0; i < elements.size(); i++) {
            String element = elements.get(i);
            int separator = element.indexOf(WEIGHT_SEPARATOR);
            String value = element;
            long weight = 1;
            if (separator >= 0) {
                weighted = true;
                value = element.substring(0, separator).strip();
                weight = parseWeight(element, element.substring(separator + 1).strip());
                ValidateUtils.isTrue(!value.isEmpty(), "Element '%s' has a weight but no value", element);
            }
            values.add(value);
            ValidateUtils.isTrue(total <= Long.MAX_VALUE - weight, "Weights add up to more than %s", Long.MAX_VALUE);
            total += weight;
            cumulativeWeights[i] = total;
        }
        return new WeightedList(elements, List.copyOf(values), weighted ? cumulativeWeights : null);
    }

    List<String> values() {
        return values;
    }

    List<String> elements() {
        return elements;
    }

    boolean isEmpty() {
        return values.isEmpty();
    }

    String pick(BaseProducer baseProducer) {
        if (cumulativeWeights == null) {
            return baseProducer.randomElement(values);
        }
        long drawn = baseProducer.randomBetween(1L, cumulativeWeights[cumulativeWeights.length - 1]);
        int index = Arrays.binarySearch(cumulativeWeights, drawn);
        return values.get(index >= 0 ? index : -index - 1);
    }

    private static long parseWeight(String element, String weight) {
        long parsed;
        try {
            parsed = Long.parseLong(weight);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                String.format("Element '%s' must have a positive integer weight", element), ex);
        }
        ValidateUtils.isTrue(parsed > 0, "Element '%s' must have a positive integer weight", element);
        return parsed;
    }
}
```

`pick` is only called on a non-empty list (Task 3 checks `isEmpty` first). If Error Prone or PMD flags something in
this file, fix it in the same step without changing behavior; the jspecify `@Nullable` annotation is already a
runtime dependency of the project.

- [ ] **Step 4: Run the spec to verify it passes**

Run: `./mvnw -q test -Dtest=WeightedListSpec`
Expected: PASS, 0 failures.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/data/WeightedList.java src/test/groovy/com/devskiller/jfairy/data/WeightedListSpec.groovy
git commit -m "feat: add weighted list parsing and picking"
```

---

### Task 3: Weighted picks in `MapBasedDataMaster`

**Files:**
- Modify: `src/main/java/com/devskiller/jfairy/data/MapBasedDataMaster.java`
- Modify: `src/main/java/com/devskiller/jfairy/data/DataMaster.java` (javadoc of `getStringList`, `getRandomValue`)
- Modify: `src/test/groovy/com/devskiller/jfairy/data/BundledDataEquivalenceSpec.groovy` (compare raw elements)
- Modify: `README.md` (section "Custom data")
- Test: `src/test/groovy/com/devskiller/jfairy/data/MapBasedDataMasterSpec.groovy`

**Interfaces:**
- Consumes: `WeightedList.parse`, `values()`, `elements()`, `isEmpty()`, `pick(BaseProducer)` from Task 2.
- Produces: package-private `List<String> getElements(String key)` and
  `List<String> getElements(String dataKey, String type)` on `MapBasedDataMaster` (elements as written, weights
  included), used by `BundledDataEquivalenceSpec`. Existing `getValues(String, String)` keeps returning bare values.

- [ ] **Step 1: Write the failing tests**

In `MapBasedDataMasterSpec.groovy`, add after `"converts the picked value to the requested class"`:

```groovy
    def "returns list values without their weights"() {
        when:
            readCustom('lastNames=Nowak*98387, Kowalski*66589, Adamiec\n')

        then:
            dataMaster.getStringList('lastNames') == ['Nowak', 'Kowalski', 'Adamiec']
            dataMaster.getElements('lastNames') == ['Nowak*98387', 'Kowalski*66589', 'Adamiec']
    }

    def "picks weighted values in proportion to their weights"() {
        given:
            MapBasedDataMaster seeded = new MapBasedDataMaster(new BaseProducer(new RandomGenerator(42)))
            readCustom(seeded, 'names=A*1,B*9\n')

        when:
            int picksOfB = (1..10_000).count { seeded.getRandomValue('names') == 'B' }

        then:
            picksOfB >= 8_500
            picksOfB <= 9_500
    }

    def "converts a weighted value to the requested class"() {
        when:
            readCustom('creditCardPrefixes.Visa=4*3\n')

        then:
            dataMaster.getValuesOfType('creditCardPrefixes', 'Visa', Integer.class) == 4
    }

    def "a typed custom key with weights replaces only its own type"() {
        given:
            dataMaster.readResources("datamaster/base.properties")

        when:
            readCustom('firstNames.male=Homer*3,Bart\n')

        then:
            dataMaster.getValues(PersonProvider.FIRST_NAME, "male") == ["Homer", "Bart"]
            dataMaster.getElements(PersonProvider.FIRST_NAME, "male") == ["Homer*3", "Bart"]
            dataMaster.getValues(PersonProvider.FIRST_NAME, "female") == ["Jane"]
    }

    def "reports a malformed weight on #access, naming the file and the key"() {
        given:
            readCustom('lastNames=Adamiec,Nowak*0\n')

        when:
            accessor(dataMaster)

        then:
            IllegalArgumentException ex = thrown()
            ex.message.endsWith("custom.properties: key 'lastNames': Element 'Nowak*0' must have a positive integer weight")

        where:
            access              | accessor
            'getStringList'     | { MapBasedDataMaster it -> it.getStringList('lastNames') }
            'getRandomValue'    | { MapBasedDataMaster it -> it.getRandomValue('lastNames') }
            'getValuesOfType'   | { MapBasedDataMaster it -> it.getValuesOfType('lastNames', 'male', String) }
    }

    def "a scalar containing an asterisk stays readable"() {
        when:
            readCustom('text=Hello *world*, how are you?\n')

        then:
            dataMaster.getString('text') == 'Hello *world*, how are you?'
    }
```

Replace the `readCustom` helper at the bottom of the spec with an overload pair:

```groovy
    private void readCustom(String content) {
        readCustom(dataMaster, content)
    }

    private void readCustom(MapBasedDataMaster master, String content) {
        Path dir = Files.createTempDirectory(tempDir, 'custom')
        Files.writeString(dir.resolve('custom.properties'), content)
        new URLClassLoader([dir.toUri().toURL()] as URL[], (ClassLoader) null).withCloseable {
            master.readResources('custom.properties', it)
        }
    }
```

- [ ] **Step 2: Run the spec to verify the new tests fail**

Run: `./mvnw -q test -Dtest=MapBasedDataMasterSpec`
Expected: FAIL. `getElements` does not exist (MissingMethodException), `getStringList` returns `Nowak*98387`,
`Integer.valueOf("4*3")` fails, and a malformed weight is not reported. `"a scalar containing an asterisk stays
readable"` passes already; it guards against failing eagerly in Step 3.

- [ ] **Step 3: Switch `MapBasedDataMaster` to `WeightedList`**

In `MapBasedDataMaster.java`:

1. Replace the class javadoc paragraph with:

```java
/**
 * {@link DataMaster} backed by flat {@code .properties} resources (generated at build time from the bundled YAML files).
 * <p>
 * A value is either a scalar ({@code language=PL}), a comma-separated list ({@code cities=A,B}) or, for data split by
 * type, one list per type ({@code firstNames.male=A,B}). List elements are trimmed and empty ones skipped. A list
 * element may carry a weight, {@code Nowak*98387}, making it picked proportionally more often by
 * {@link #getRandomValue} and {@link #getValuesOfType}; an element without a weight counts as 1. Keys are
 * case-insensitive.
 */
```

2. Replace the fields after `PROPERTIES_EXTENSION`/`LEGACY_YAML_EXTENSION` (drop `LIST_SEPARATOR`, it moved to
   `WeightedList`):

```java
    private final BaseProducer baseProducer;
    private final Map<String, String> dataSource = new HashMap<>();
    // Resource each key was last read from, to name it in errors
    private final Map<String, URL> origins = new HashMap<>();
    // Every value split once when resources are read, so lookups never write and a created Fairy may be shared between
    // threads. readResources itself is not thread-safe: it is only meant to be called while bootstrapping.
    private Map<String, WeightedList> lists = Map.of();
    // A value that is not a valid list only fails when used as one: .properties cannot tell a list from a scalar such
    // as text, which may legitimately contain '*'
    private Map<String, IllegalArgumentException> invalidLists = Map.of();
```

3. Replace `getStringList`, `getValuesOfType` and `getRandomValue`:

```java
    @Override
    public List<String> getStringList(String key) {
        return list(key).values();
    }

    @Override
    public <T> T getValuesOfType(String dataKey, final String type, final Class<T> resultClass) {
        String value = typedList(dataKey, type).pick(baseProducer);
        return convert(value, resultClass, dataKey, type);
    }

    @Override
    public String getRandomValue(String key) {
        return list(key).pick(baseProducer);
    }
```

   Keep the existing javadoc comments above `getStringList` and `getValuesOfType`.

4. In `readResources(String, ClassLoader)`, replace the loop and the final assignment:

```java
        for (URL url : urls) {
            appendData(load(url), url);
        }
        splitAll();
```

5. Replace `getValues` and add the new accessors and lookups right after it:

```java
    /**
     * Returns the values stored under {@code dataKey.type} or, when the data is not split by type, under {@code dataKey}.
     */
    List<String> getValues(String dataKey, String type) {
        return typedList(dataKey, type).values();
    }

    /**
     * Returns the elements of {@code key} as written, weights included.
     */
    List<String> getElements(String key) {
        return list(key).elements();
    }

    /**
     * Returns the elements of {@code dataKey.type}, or of {@code dataKey} when not split by type, as written.
     */
    List<String> getElements(String dataKey, String type) {
        return typedList(dataKey, type).elements();
    }

    private WeightedList typedList(String dataKey, String type) {
        ValidateUtils.notNull(dataKey, "key cannot be null");
        String typedKey = dataKey + TYPE_SEPARATOR + type;
        if (dataSource.containsKey(normalize(typedKey))) {
            return list(typedKey);
        }
        ValidateUtils.isTrue(dataSource.containsKey(normalize(dataKey)), "No such key: %s nor %s", typedKey, dataKey);
        return list(dataKey);
    }

    private WeightedList list(String key) {
        getData(key);
        String normalized = normalize(key);
        IllegalArgumentException invalid = invalidLists.get(normalized);
        if (invalid != null) {
            throw new IllegalArgumentException(
                String.format("%s: key '%s': %s", origins.get(normalized), key, invalid.getMessage()), invalid);
        }
        WeightedList list = lists.get(normalized);
        ValidateUtils.isTrue(!list.isEmpty(), "No values for key: %s", key);
        return list;
    }
```

6. Replace `appendData` and `splitAll`:

```java
    private void appendData(Properties data, URL url) {
        for (String key : data.stringPropertyNames()) {
            String normalized = normalize(key);
            if (normalized.indexOf(TYPE_SEPARATOR) < 0) {
                dataSource.keySet().removeIf(existing -> rootOf(existing).equals(normalized));
            }
        }
        for (String key : data.stringPropertyNames()) {
            dataSource.put(normalize(key), data.getProperty(key));
            origins.put(normalize(key), url);
        }
        origins.keySet().retainAll(dataSource.keySet());
    }

    private void splitAll() {
        Map<String, WeightedList> parsed = new HashMap<>();
        Map<String, IllegalArgumentException> invalid = new HashMap<>();
        dataSource.forEach((key, value) -> {
            try {
                parsed.put(key, WeightedList.parse(value));
            } catch (IllegalArgumentException ex) {
                invalid.put(key, ex);
            }
        });
        lists = Collections.unmodifiableMap(parsed);
        invalidLists = Collections.unmodifiableMap(invalid);
    }
```

   Keep the javadoc of `appendData` as it is. Remove the now unused `import java.util.Arrays;`.

In `DataMaster.java`, replace the javadoc of `getStringList` and `getRandomValue`:

```java
    /**
     * Returns the values of the list associated with the given key, without their weights. Picking from this list
     * yourself is uniform; use {@link #getRandomValue(String)} for a pick that honours weights.
     *
     * @param key the unique identifier for the data entries
     * @return a list of string values found for the key
     */
    List<String> getStringList(String key);
```

```java
    /**
     * Selects a random value from the entries associated with the given key, honouring their weights if the list has
     * any.
     *
     * @param key the unique identifier for the data list
     * @return a randomly selected string value
     */
    String getRandomValue(String key);
```

In `BundledDataEquivalenceSpec.groovy`, change `hasSameData` to compare raw elements, so weights are checked too:

```groovy
    private static boolean hasSameData(MapBasedDataMaster dataMaster, String key, Object value) {
        if (value instanceof Map) {
            value.each { type, list -> assert dataMaster.getElements(key, type as String) == list*.toString() }
        } else if (value instanceof List) {
            assert dataMaster.getElements(key) == value*.toString()
        } else {
            assert dataMaster.getString(key) == value.toString()
        }
        true
    }
```

In `README.md`, section "Custom data", insert after the paragraph that ends with "Files are read as UTF-8." and
before the `withFilePrefix` sentence:

```markdown
A list element may carry a weight to be picked more often: with `lastNames=Nowak*98387,Kowalski*66589,Adamiec`,
Nowak comes up about 98387 times as often as Adamiec, since an element without a weight counts as 1. Weights are
positive whole numbers, relative within one list, and a list without any weight is picked uniformly. A malformed
weight fails when the key is first used, naming the file and the key.
```

- [ ] **Step 4: Run the data specs to verify they pass**

Run: `./mvnw -q test -Dtest='MapBasedDataMasterSpec,WeightedListSpec,BundledDataEquivalenceSpec,Yaml2PropertiesScriptSpec'`
Expected: PASS, 0 failures.

- [ ] **Step 5: Run the full build to prove unweighted lists pick exactly as before**

Run: `./mvnw -q verify`
Expected: BUILD SUCCESS with no change to any seeded snapshot (`FairyDeSpec`, `FairyFrSpec`, `FairyJaSpec`,
`FairyKaSpec`, `FairyZhSpec`): no bundled list has a weight yet, so every pick still goes through `randomElement`.
A failing snapshot here means the uniform path changed; fix the code, not the snapshot.

- [ ] **Step 6: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/data/MapBasedDataMaster.java src/main/java/com/devskiller/jfairy/data/DataMaster.java \
    src/test/groovy/com/devskiller/jfairy/data/MapBasedDataMasterSpec.groovy \
    src/test/groovy/com/devskiller/jfairy/data/BundledDataEquivalenceSpec.groovy README.md
git commit -m "feat: pick weighted data values in proportion to their weights"
```

---

### Task 4: PESEL to YAML script and Maven profile

**Files:**
- Create: `build/pesel2yaml.groovy`
- Modify: `pom.xml` (new profile `pesel` inside `<profiles>`, before `</profiles>` at the end of the file)
- Test: `src/test/groovy/com/devskiller/jfairy/data/Pesel2YamlScriptSpec.groovy`

**Interfaces:**
- Consumes: nothing from other tasks at runtime. The YAML it writes (`value*weight` elements, block style) must pass
  Task 1's `yaml2properties.groovy`; Task 5 checks that on the real file.
- Produces: script bindings `yamlFile`, `dataDate`, `firstNamesMale`, `firstNamesFemale`, `lastNamesMale`,
  `lastNamesFemale` (URL strings, `file:` or `https:`), `firstNamesLimit`, `lastNamesLimit`; Maven command
  `./mvnw -Ppesel initialize`.

CSV facts (checked on the 2026-01-20 data): UTF-8 without BOM, CRLF line ends, comma separated, no quoting, one
header row. First names: `IMIĘ_PIERWSZE,PŁEĆ,LICZBA_WYSTĄPIEŃ`; last names: `Nazwisko aktualne,Liczba`. Names are
upper case. The name is always the first column and the count the last.

- [ ] **Step 1: Write the failing spec**

Create `src/test/groovy/com/devskiller/jfairy/data/Pesel2YamlScriptSpec.groovy`:

```groovy
package com.devskiller.jfairy.data

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import spock.lang.Specification
import spock.lang.TempDir

/**
 * Runs build/pesel2yaml.groovy against small CSV fixtures, without network access.
 */
class Pesel2YamlScriptSpec extends Specification {

    private static final Path SCRIPT = Paths.get('build', 'pesel2yaml.groovy')

    private static final String YAML = '''\
alphabet: abc
firstNames: {
  male: [Adam],
  female: [Anna]
}
lastNames: {
  male: [Adamiec],
  female: [Adamiec]
}

cities: [Kraków]
'''

    private static final String EXPECTED = '''\
alphabet: abc
# PESEL registry (dane.gov.pl, CC0), state as of 2026-01-20; regenerate with ./mvnw -Ppesel initialize
firstNames:
    male:
    - Piotr*682516
    - Krzysztof*631898
    female:
    - Maria*2000
    - Anna*1000

lastNames:
    male:
    - Nowak*98387
    - Kowalski*66589
    female:
    - Nowak-Kowalska*10
    - O'Brien*5

cities: [Kraków]
'''

    @TempDir
    Path dir

    def "replaces the name blocks with the most frequent names, title-cased and weighted"() {
        given:
            Path yaml = write('jfairy_pl.yml', YAML)

        when:
            run(yaml)

        then:
            Files.readString(yaml, StandardCharsets.UTF_8) == EXPECTED
    }

    def "gives the same file when run again"() {
        given:
            Path yaml = write('jfairy_pl.yml', YAML)

        when:
            run(yaml)
            run(yaml)

        then:
            Files.readString(yaml, StandardCharsets.UTF_8) == EXPECTED
    }

    def "fails instead of writing an empty block when a source has no usable rows"() {
        given:
            Path yaml = write('jfairy_pl.yml', YAML)

        when:
            run(yaml, [lastNamesFemale: csv('<html>Not found</html>\r\n')])

        then:
            Exception ex = thrown()
            ex.message.contains('no names')
            Files.readString(yaml, StandardCharsets.UTF_8) == YAML
    }

    def "fails when the file has no name blocks"() {
        given:
            Path yaml = write('jfairy_pl.yml', 'alphabet: abc\n')

        when:
            run(yaml)

        then:
            Exception ex = thrown()
            ex.message.contains('firstNames')
    }

    private void run(Path yaml, Map<String, String> overrides = [:]) {
        Map<String, Object> variables = [
            yamlFile        : yaml.toString(),
            dataDate        : '2026-01-20',
            firstNamesLimit : '2',
            lastNamesLimit  : '2',
            firstNamesMale  : csv('IMIĘ_PIERWSZE,PŁEĆ,LICZBA_WYSTĄPIEŃ\r\nPIOTR,MĘŻCZYZNA,682516\r\n'
                + 'BRAK DANYCH,MĘŻCZYZNA,900000\r\nKRZYSZTOF,MĘŻCZYZNA,631898\r\nTOMASZ,MĘŻCZYZNA,533217\r\n'),
            firstNamesFemale: csv('IMIĘ_PIERWSZE,PŁEĆ,LICZBA_WYSTĄPIEŃ\r\nANNA,KOBIETA,1000\r\nMARIA,KOBIETA,2000\r\n'),
            lastNamesMale   : csv('Nazwisko aktualne,Liczba\r\nKOWALSKI,66589\r\nNOWAK,98387\r\nWIŚNIEWSKI,53079\r\n'),
            lastNamesFemale : csv("Nazwisko aktualne,Liczba\r\nNOWAK-KOWALSKA,10\r\nO'BRIEN,5\r\nŻÓŁĆ1,7\r\n"),
        ]
        variables.putAll(overrides)
        new GroovyShell(new Binding(variables)).evaluate(SCRIPT.toFile())
    }

    private String csv(String content) {
        Path file = Files.createTempFile(dir, 'pesel', '.csv')
        Files.writeString(file, content, StandardCharsets.UTF_8)
        file.toUri().toString()
    }

    private Path write(String name, String content) {
        Files.writeString(dir.resolve(name), content, StandardCharsets.UTF_8)
    }
}
```

The fixtures cover: the header row (count not numeric) is skipped, `BRAK DANYCH` is dropped despite the highest
count, `ŻÓŁĆ1` is dropped, sorting is by count descending, the limit is 2, hyphen and apostrophe start a new capital,
the blank line before `cities` survives, and no weight collides with the `firstNames`/`lastNames` keys of the flow
blocks.

- [ ] **Step 2: Run the spec to verify it fails**

Run: `./mvnw -q test -Dtest=Pesel2YamlScriptSpec`
Expected: FAIL, `FileNotFoundException` for `build/pesel2yaml.groovy`.

- [ ] **Step 3: Write the script**

Create `build/pesel2yaml.groovy`:

```groovy
/*
 * Regenerates the firstNames and lastNames blocks of jfairy_pl.yml from the PESEL registry statistics published on
 * dane.gov.pl by Ministerstwo Cyfryzacji under CC0 1.0, weighting every name by the number of living people who bear
 * it.
 *
 * Not part of the normal build: it needs the network and runs about once a year, with
 *   ./mvnw -Ppesel initialize
 * after pointing the pesel.* properties in pom.xml at the newest CSV resources of:
 *   last names:  https://dane.gov.pl/pl/dataset/1681  ("Nazwiska męskie/żeńskie - stan na ...")
 *   first names: https://dane.gov.pl/pl/dataset/1667  ("lista imion męskich/żeńskich ... - imię pierwsze")
 * The CSV of resource <id> is served at https://api.dane.gov.pl/resources/<id>/csv.
 *
 * Bound variables (gmavenplus <properties> or a GroovyShell Binding):
 *   yamlFile                         - the jfairy_pl.yml to update
 *   dataDate                         - date of the PESEL state, written into the header comment
 *   firstNamesMale, firstNamesFemale - CSV URLs with columns name, sex, count
 *   lastNamesMale, lastNamesFemale   - CSV URLs with columns name, count
 *   firstNamesLimit, lastNamesLimit  - how many of the most frequent names to keep per sex
 */

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import groovy.transform.Field

@Field static final String MARKER = '# PESEL registry'
@Field static final Locale POLISH = Locale.forLanguageTag('pl')
@Field static final List<String> NAME_KEYS = ['firstNames', 'lastNames']

// Letters, optionally joined by a hyphen or an apostrophe: drops rows such as 'BRAK DANYCH'
boolean isName(String name) {
    name ==~ /\p{L}+(?:[-']\p{L}+)*/
}

// 'NOWAK-KOWALSKA' -> 'Nowak-Kowalska', "O'BRIEN" -> "O'Brien"
String titleCase(String name) {
    name.toLowerCase(POLISH).split(/(?<=[-'])/).collect { String part ->
        part.substring(0, 1).toUpperCase(POLISH) + part.substring(1)
    }.join('')
}

List<String> weightedNames(String url, int limit) {
    String content = URI.create(url).toURL().openStream().withCloseable { new String(it.readAllBytes(), StandardCharsets.UTF_8) }
    Map<String, Long> counts = [:]
    content.readLines().each { String line ->
        List<String> columns = line.split(',') as List
        String name = columns.first().strip()
        String count = columns.last().strip()
        if (columns.size() > 1 && count ==~ /[0-9]+/ && isName(name)) {
            counts.merge(titleCase(name), count as long, Long::sum)
        }
    }
    if (counts.isEmpty()) {
        throw new IllegalStateException("${url}: no names found")
    }
    counts.entrySet()
        .sort { a, b -> b.value <=> a.value ?: a.key <=> b.key }
        .take(limit)
        .collect { "${it.key}*${it.value}".toString() }
}

List<String> block(String key, List<String> male, List<String> female) {
    List<String> lines = ["${key}:".toString()]
    [male: male, female: female].each { String type, List<String> names ->
        lines << "    ${type}:".toString()
        names.each { lines << "    - ${it}".toString() }
    }
    lines << ''
    lines
}

// Drops the old name blocks, blank lines up to the next key included, and puts the new ones where the first was
String replaceBlocks(String yaml, String header, List<String> blocks) {
    List<String> kept = []
    boolean skipping = false
    boolean replaced = false
    yaml.readLines().each { String line ->
        if (line.startsWith(MARKER)) {
            return
        }
        def topLevelKey = line =~ /^([A-Za-z][A-Za-z0-9]*):/
        if (topLevelKey.find()) {
            skipping = topLevelKey.group(1) in NAME_KEYS
            if (skipping && !replaced) {
                kept << header
                kept.addAll(blocks)
                replaced = true
            }
        }
        if (!skipping) {
            kept << line
        }
    }
    if (!replaced) {
        throw new IllegalStateException("no ${NAME_KEYS.join(' or ')} block to replace")
    }
    kept.join('\n') + '\n'
}

Path yaml = Paths.get(yamlFile as String)
int firstLimit = firstNamesLimit as int
int lastLimit = lastNamesLimit as int

// Read every source before touching the file, so a failed download leaves it as it was
List<String> blocks = block('firstNames', weightedNames(firstNamesMale as String, firstLimit),
    weightedNames(firstNamesFemale as String, firstLimit)) +
    block('lastNames', weightedNames(lastNamesMale as String, lastLimit),
        weightedNames(lastNamesFemale as String, lastLimit))
String header = "${MARKER} (dane.gov.pl, CC0), state as of ${dataDate}; regenerate with ./mvnw -Ppesel initialize"

Files.writeString(yaml, replaceBlocks(Files.readString(yaml, StandardCharsets.UTF_8), header, blocks), StandardCharsets.UTF_8)
```

`"fails when the file has no name blocks"` expects the exception before any write; `replaceBlocks` throws before
`Files.writeString`, so that holds.

- [ ] **Step 4: Run the spec to verify it passes**

Run: `./mvnw -q test -Dtest=Pesel2YamlScriptSpec`
Expected: PASS, 4 features, 0 failures. If `EXPECTED` differs only in blank lines, fix the script (the expected
layout is the contract Task 5 relies on), not the fixture.

- [ ] **Step 5: Add the Maven profile**

In `pom.xml`, insert before `</profiles>` (the last profile ends with `</profile>` just above it):

```xml
		<profile>
			<!-- Regenerates the pl name blocks from the PESEL registry: ./mvnw -Ppesel initialize -->
			<id>pesel</id>
			<properties>
				<pesel.dataDate>2026-01-20</pesel.dataDate>
				<pesel.firstNamesMale>https://api.dane.gov.pl/resources/1159669/csv</pesel.firstNamesMale>
				<pesel.firstNamesFemale>https://api.dane.gov.pl/resources/1159670/csv</pesel.firstNamesFemale>
				<pesel.lastNamesMale>https://api.dane.gov.pl/resources/1148808/csv</pesel.lastNamesMale>
				<pesel.lastNamesFemale>https://api.dane.gov.pl/resources/1148811/csv</pesel.lastNamesFemale>
				<pesel.firstNamesLimit>500</pesel.firstNamesLimit>
				<pesel.lastNamesLimit>1000</pesel.lastNamesLimit>
			</properties>
			<build>
				<plugins>
					<plugin>
						<groupId>org.codehaus.gmavenplus</groupId>
						<artifactId>gmavenplus-plugin</artifactId>
						<executions>
							<execution>
								<id>pesel-to-yaml</id>
								<goals>
									<goal>execute</goal>
								</goals>
								<phase>initialize</phase>
								<configuration>
									<properties>
										<property><name>yamlFile</name><value>${project.basedir}/src/main/resources/jfairy_pl.yml</value></property>
										<property><name>dataDate</name><value>${pesel.dataDate}</value></property>
										<property><name>firstNamesMale</name><value>${pesel.firstNamesMale}</value></property>
										<property><name>firstNamesFemale</name><value>${pesel.firstNamesFemale}</value></property>
										<property><name>lastNamesMale</name><value>${pesel.lastNamesMale}</value></property>
										<property><name>lastNamesFemale</name><value>${pesel.lastNamesFemale}</value></property>
										<property><name>firstNamesLimit</name><value>${pesel.firstNamesLimit}</value></property>
										<property><name>lastNamesLimit</name><value>${pesel.lastNamesLimit}</value></property>
									</properties>
									<scripts>
										<script>${project.basedir}/build/pesel2yaml.groovy</script>
									</scripts>
								</configuration>
							</execution>
						</executions>
					</plugin>
				</plugins>
			</build>
		</profile>
```

The resource ids are the 2026-01-20 CSVs (1159669/1159670: first names male/female, "imię pierwsze";
1148808/1148811: last names male/female); each `…/csv` URL answered HTTP 200 when this plan was written.

- [ ] **Step 6: Verify the profile resolves without running it**

Run: `./mvnw -q help:effective-pom -Ppesel -Doutput=target/effective-pom.xml && grep -c pesel-to-yaml target/effective-pom.xml`
Expected: `1`. A normal build must not run the script: `./mvnw -q verify` still succeeds and `git status` shows
`jfairy_pl.yml` unchanged.

- [ ] **Step 7: Commit**

```bash
git add build/pesel2yaml.groovy pom.xml src/test/groovy/com/devskiller/jfairy/data/Pesel2YamlScriptSpec.groovy
git commit -m "build: add script regenerating pl names from the PESEL registry"
```

---

### Task 5: Regenerate the `pl` names

**Files:**
- Modify: `src/main/resources/jfairy_pl.yml` (generated `firstNames` and `lastNames` blocks)
- Modify: seeded specs that pin `pl` values, only if Step 3 shows them failing

**Interfaces:**
- Consumes: `./mvnw -Ppesel initialize` from Task 4, weight validation from Task 1, weighted picks from Task 3.
- Produces: the final `pl` data.

- [ ] **Step 1: Run the generator**

Run: `./mvnw -q -Ppesel initialize`
Expected: success; `git diff --stat` shows only `src/main/resources/jfairy_pl.yml` changed.

- [ ] **Step 2: Inspect the generated data**

Run:

```bash
grep -n "PESEL registry\|^firstNames:\|^lastNames:\|^    male:\|^    female:" src/main/resources/jfairy_pl.yml
sed -n '/^lastNames:/,/^    female:/p' src/main/resources/jfairy_pl.yml | head -6
```

Expected: one header comment with `2026-01-20`; `firstNames` with 500 `male` and 500 `female` entries, `lastNames`
with 1000 and 1000; last names starting `Nowak*…`, `Kowalski*…` (female: `Nowak*…`, `Kowalska*…`); first names
starting `Piotr*…` for men. Every other key of the file is unchanged in `git diff`. Count the entries with:

```bash
awk '/^firstNames:|^lastNames:/{k=$1} /^    (male|female):/{t=$1} /^    - /{n[k t]++} END{for (x in n) print x, n[x]}' src/main/resources/jfairy_pl.yml
```

- [ ] **Step 3: Run the full build**

Run: `./mvnw -q verify`
Expected: `yaml2properties.groovy` accepts the file (a rejection names the offending element; fix the script in Task 4
style, not the data by hand), `BundledDataEquivalenceSpec` and `GeneratedValuesVarySpec` pass. Seeded specs that
depend on the `pl` random sequence may fail, because a weighted pick consumes a `nextLong` instead of a `nextInt`.

- [ ] **Step 4: Update seeded `pl` expectations, if any failed**

For each failing seeded assertion, confirm that the new actual value comes from the new data or is a later value in
the same `pl` random sequence (for example a PESEL or an address that shifted because the name pick consumed a
different draw), then replace the expected literal with the actual one. Do not touch a snapshot of any other locale:
a failure there means Task 3 broke the uniform path. Re-run `./mvnw -q verify` until it is green.

- [ ] **Step 5: Commit**

```bash
git add src/main/resources/jfairy_pl.yml src/test
git commit -m "feat: weight pl first and last names by PESEL registry frequency

Names come from the PESEL registry state of 2026-01-20 published on
dane.gov.pl under CC0: the 500 most frequent first names and the 1000
most frequent last names per sex, each weighted by the number of living
people who bear it."
```
