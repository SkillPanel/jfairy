/*
 * Converts the jFairy YAML data files into flat .properties resources read by MapBasedDataMaster.
 *
 * Bound variables (gmavenplus <properties> or a GroovyShell Binding):
 *   sourceDir - directory containing the *.yml files
 *   outputDir - directory receiving the *.properties files
 *
 * Output format:
 *   scalar        key=value
 *   list          key=a,b,c
 *   map of lists  key.subKey=a,b,c
 * Anything else fails the build, naming the file and key. Keys are sorted and no timestamp is written,
 * so the output is reproducible.
 */

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings

void check(boolean condition, String file, String key, String message) {
    if (!condition) {
        throw new IllegalStateException("${file}: key '${key}': ${message}")
    }
}

String describe(Object value) {
    value == null ? 'null' : "${value.getClass().simpleName} ${value}"
}

String scalar(String file, String key, Object value) {
    check(value instanceof String || value instanceof Number, file, key, "expected a string or a number but got ${describe(value)}")
    value.toString()
}

String joinList(String file, String key, List values) {
    check(!values.isEmpty(), file, key, 'list must not be empty')
    values.collect { element ->
        String text = scalar(file, key, element)
        check(!text.isEmpty() && !text.contains(',') && !text.contains('\n'), file, key,
            "list element must be non-empty and contain neither ',' nor a newline: [${text}]")
        text
    }.join(',')
}

String propertyLine(String key, String value) {
    // Reuse the JDK's escaping; store() also emits a timestamp comment, which is dropped
    Properties single = new Properties()
    single.setProperty(key, value)
    StringWriter writer = new StringWriter()
    single.store(writer, null)
    writer.toString().readLines().find { !it.startsWith('#') }
}

// MapBasedDataMaster reads keys case-insensitively, so such keys would silently shadow each other
void checkNoCaseDuplicates(String file, String prefix, Collection keys) {
    Map<String, String> seen = [:]
    keys.each { rawKey ->
        String key = "${prefix}${rawKey}"
        String other = seen.put(key.toLowerCase(Locale.ROOT), key)
        check(other == null, file, key, "differs from '${other}' only in case")
    }
}

Map<String, String> flatten(String file, Map document) {
    Map<String, String> flat = new TreeMap<>()
    checkNoCaseDuplicates(file, '', document.keySet())
    document.each { rawKey, value ->
        String key = rawKey as String
        check(!key.contains('.'), file, key, "key must not contain '.'")
        if (value instanceof Map) {
            checkNoCaseDuplicates(file, "${key}.", (value as Map).keySet())
            (value as Map).each { rawSubKey, subValue ->
                String subKey = rawSubKey as String
                String nestedKey = "${key}.${subKey}"
                check(!subKey.contains('.'), file, nestedKey, "key must not contain '.'")
                check(subValue instanceof List, file, nestedKey, "nested value must be a list but got ${describe(subValue)}")
                flat[nestedKey] = joinList(file, nestedKey, subValue as List)
            }
        } else if (value instanceof List) {
            flat[key] = joinList(file, key, value as List)
        } else {
            flat[key] = scalar(file, key, value)
        }
    }
    flat
}

Path source = Paths.get(sourceDir as String)
Path target = Paths.get(outputDir as String)
Files.createDirectories(target)

List<Path> yamlFiles = Files.list(source).withCloseable { stream ->
    stream.filter { it.fileName.toString().endsWith('.yml') }.sorted().toList()
}

for (Path yamlFile : yamlFiles) {
    String file = yamlFile.fileName.toString()
    LoadSettings settings = LoadSettings.builder().setAllowDuplicateKeys(false).setLabel(file).build()
    Object document
    try {
        document = new Load(settings).loadFromString(Files.readString(yamlFile, StandardCharsets.UTF_8))
    } catch (RuntimeException ex) {
        throw new IllegalStateException("${file}: cannot parse YAML: ${ex.message}", ex)
    }
    check(document instanceof Map, file, '<root>', 'top level must be a mapping')

    Map<String, String> flat = flatten(file, document as Map)
    Path out = target.resolve(file.replaceFirst(/\.yml$/, '.properties'))
    Files.write(out, flat.collect { key, value -> propertyLine(key, value) }, StandardCharsets.UTF_8)
}
