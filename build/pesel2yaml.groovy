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
        // Any unindented line other than the closing brace of a flow block starts a new entry or comment
        if (line ==~ /^[^\s}\]].*/) {
            skipping = NAME_KEYS.any { line.startsWith("${it}:") }
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
