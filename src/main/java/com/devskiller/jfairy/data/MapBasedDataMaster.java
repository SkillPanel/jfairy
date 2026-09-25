/*
 * Copyright (c) 2013 Codearte
 */

package com.devskiller.jfairy.data;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.util.LanguageCode;
import com.devskiller.jfairy.producer.util.ValidateUtils;

/**
 * {@link DataMaster} backed by flat {@code .properties} resources (generated at build time from the bundled YAML files).
 * <p>
 * A value is either a scalar ({@code language=PL}), a comma-separated list ({@code cities=A,B}) or, for data split by
 * type, one list per type ({@code firstNames.male=A,B}). List elements are trimmed and empty ones skipped. Keys are
 * case-insensitive.
 */
public class MapBasedDataMaster implements DataMaster {

    public static final String LANGUAGE_TAG = "language";

    private static final Logger LOG = LoggerFactory.getLogger(MapBasedDataMaster.class);
    private static final String LIST_SEPARATOR = ",";
    private static final char TYPE_SEPARATOR = '.';
    private static final String PROPERTIES_EXTENSION = ".properties";
    private static final String LEGACY_YAML_EXTENSION = ".yml";

    private final BaseProducer baseProducer;
    private final Map<String, String> dataSource = new HashMap<>();
    // Every value split once when resources are read, so lookups never write (a Fairy may be shared between threads)
    private Map<String, List<String>> lists = Map.of();

    public MapBasedDataMaster(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    /**
     * Returns list (null safe) of elements for desired key from dataSource files
     *
     * @param key desired node key
     * @return list of elements for desired key
     * @throws IllegalArgumentException if no element for key has been found
     */
    @Override
    public List<String> getStringList(String key) {
        getData(key);
        List<String> list = lists.get(normalize(key));
        ValidateUtils.isTrue(!list.isEmpty(), "No values for key: %s", key);
        return list;
    }

    /**
     * Picks a random value of {@code dataKey}/{@code type} and returns it as {@code resultClass}: values are strings,
     * so any other class must have a public static {@code valueOf(String)} (e.g. {@code Integer}, an enum).
     */
    @Override
    public <T> T getValuesOfType(String dataKey, final String type, final Class<T> resultClass) {
        String value = baseProducer.randomElement(getValues(dataKey, type));
        return convert(value, resultClass, dataKey, type);
    }

    /**
     * Returns element (null safe) for desired key from dataSource files
     *
     * @param key desired node key
     * @return string element for desired key
     * @throws IllegalArgumentException if no element for key has been found
     */
    @Override
    public String getString(String key) {
        return getData(key);
    }

    @Override
    public String getRandomValue(String key) {
        return baseProducer.randomElement(getStringList(key));
    }

    @Override
    public LanguageCode getLanguage() {
        String tag = getString(LANGUAGE_TAG).toUpperCase(Locale.ROOT);
        try {
            return LanguageCode.valueOf(tag);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown language tag: " + tag, ex);
        }
    }

    //fixme - should be package-private
    public void readResources(String path) throws IOException {
        readResources(path, getClass().getClassLoader());
    }

    void readResources(String path, ClassLoader classLoader) throws IOException {
        Enumeration<URL> resources = classLoader.getResources(path);
        String legacyYamlPath = legacyYamlPath(path);
        boolean legacyYamlPresent = legacyYamlPath != null && classLoader.getResource(legacyYamlPath) != null;

        if (!resources.hasMoreElements()) {
            if (legacyYamlPresent) {
                throw new IllegalArgumentException(String.format(
                    "File %s was not found on classpath, but %s was. YAML data files are no longer supported,"
                        + " convert it to .properties (see the \"Custom data\" section of the README)", path, legacyYamlPath));
            }
            throw new IllegalArgumentException(String.format("File %s was not found on classpath", path));
        }
        if (legacyYamlPresent) {
            LOG.warn("Ignoring {} on classpath: YAML data files are no longer supported, use {} instead", legacyYamlPath, path);
        }

        // Applied last-to-first, so a file earlier on the classpath (e.g. the user's own) overrides the bundled one
        List<URL> urls = Collections.list(resources);
        Collections.reverse(urls);
        for (URL url : urls) {
            appendData(load(url));
        }
        lists = splitAll(dataSource);
    }

    /**
     * Returns the list stored under {@code dataKey.type} or, when the data is not split by type, under {@code dataKey}.
     */
    List<String> getValues(String dataKey, String type) {
        ValidateUtils.notNull(dataKey, "key cannot be null");
        String typedKey = dataKey + TYPE_SEPARATOR + type;
        if (dataSource.containsKey(normalize(typedKey))) {
            return getStringList(typedKey);
        }
        ValidateUtils.isTrue(dataSource.containsKey(normalize(dataKey)), "No such key: %s nor %s", typedKey, dataKey);
        return getStringList(dataKey);
    }

    int size() {
        return dataSource.size();
    }

    private String getData(String key) {
        ValidateUtils.notNull(key, "key cannot be null");
        String value = dataSource.get(normalize(key));
        ValidateUtils.isTrue(value != null, "No such key: %s", key);
        return value;
    }

    /**
     * Merges one resource: a typed key ({@code lastNames.male}) replaces only that type, while a flat key
     * ({@code lastNames}) replaces the whole root, so it also drops the existing {@code lastNames.male} list. A typed
     * key over a flat list keeps the flat list for the other types.
     */
    private void appendData(Properties data) {
        for (String key : data.stringPropertyNames()) {
            String normalized = normalize(key);
            if (normalized.indexOf(TYPE_SEPARATOR) < 0) {
                dataSource.keySet().removeIf(existing -> rootOf(existing).equals(normalized));
            }
        }
        for (String key : data.stringPropertyNames()) {
            dataSource.put(normalize(key), data.getProperty(key));
        }
    }

    private static Map<String, List<String>> splitAll(Map<String, String> data) {
        Map<String, List<String>> result = new HashMap<>();
        data.forEach((key, value) -> result.put(key, Arrays.stream(value.split(LIST_SEPARATOR))
            .map(String::strip)
            .filter(element -> !element.isEmpty())
            .toList()));
        return Collections.unmodifiableMap(result);
    }

    private static <T> T convert(String value, Class<T> resultClass, String dataKey, String type) {
        if (resultClass.isInstance(value)) {
            return resultClass.cast(value);
        }
        IllegalArgumentException failure = new IllegalArgumentException(String.format(
            "Cannot convert '%s' (key %s, type %s) to %s", value, dataKey, type, resultClass.getName()));
        try {
            Method valueOf = resultClass.getMethod("valueOf", String.class);
            if (Modifier.isStatic(valueOf.getModifiers()) && resultClass.isAssignableFrom(valueOf.getReturnType())) {
                return resultClass.cast(valueOf.invoke(null, value));
            }
        } catch (InvocationTargetException ex) {
            failure.initCause(ex.getCause());
        } catch (ReflectiveOperationException ex) {
            failure.initCause(ex);
        }
        throw failure;
    }

    private static Properties load(URL url) throws IOException {
        Properties properties = new Properties();
        try (Reader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
            properties.load(reader);
        }
        return properties;
    }

    private static String legacyYamlPath(String path) {
        return path.endsWith(PROPERTIES_EXTENSION)
            ? path.substring(0, path.length() - PROPERTIES_EXTENSION.length()) + LEGACY_YAML_EXTENSION
            : null;
    }

    private static String rootOf(String key) {
        int separator = key.indexOf(TYPE_SEPARATOR);
        return separator < 0 ? key : key.substring(0, separator);
    }

    private static String normalize(String key) {
        return key.toLowerCase(Locale.ROOT);
    }
}
