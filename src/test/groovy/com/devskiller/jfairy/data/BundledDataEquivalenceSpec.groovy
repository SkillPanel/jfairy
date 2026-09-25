package com.devskiller.jfairy.data

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import spock.lang.Specification

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

/**
 * Guards the build-time YAML to .properties conversion: for the base file and every locale, the generated
 * resources must expose exactly the data of the YAML sources, merged the way the former YAML loader merged them
 * (locale file replaces base keys as a whole).
 */
class BundledDataEquivalenceSpec extends Specification {

    private static final Path YAML_DIR = Paths.get('src', 'main', 'resources')

    def "generated data for locale '#locale' matches the YAML sources"() {
        given:
            Map<String, Object> yaml = loadYaml('jfairy.yml')
            MapBasedDataMaster dataMaster = new MapBasedDataMaster(new BaseProducer(new RandomGenerator()))
            dataMaster.readResources('jfairy.properties')
            if (locale) {
                yaml.putAll(loadYaml("jfairy_${locale}.yml"))
                dataMaster.readResources("jfairy_${locale}.properties")
            }

        expect:
            yaml.every { key, value -> hasSameData(dataMaster, key, value) }
            dataMaster.size() == yaml.values().sum { it instanceof Map ? it.size() : 1 }

        where:
            locale << [''] + locales()
    }

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

    private static List<String> locales() {
        YAML_DIR.toFile().list().findResults { String name ->
            def matcher = name =~ /^jfairy_([a-z]+)\.yml$/
            matcher.matches() ? matcher.group(1) : null
        }.sort()
    }

    private static Map<String, Object> loadYaml(String name) {
        String content = Files.readString(YAML_DIR.resolve(name), StandardCharsets.UTF_8)
        new Load(LoadSettings.builder().build()).loadFromString(content) as Map<String, Object>
    }
}
