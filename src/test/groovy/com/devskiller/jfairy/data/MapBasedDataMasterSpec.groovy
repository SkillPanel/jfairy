/*
 * Copyright (c) 2013. Codearte
 */

package com.devskiller.jfairy.data

import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

import spock.lang.Specification
import spock.lang.TempDir

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator
import com.devskiller.jfairy.producer.person.PersonProvider
import com.devskiller.jfairy.producer.util.LanguageCode

class MapBasedDataMasterSpec extends Specification {

    private MapBasedDataMaster dataMaster = new MapBasedDataMaster(new BaseProducer(new RandomGenerator()))

    @TempDir
    Path tempDir

    def "should read first names"() {
        when:
            dataMaster.readResources("jfairy_en.properties")

        then:
            !dataMaster.getValues(PersonProvider.FIRST_NAME, "male").isEmpty()
            !dataMaster.getValues(PersonProvider.FIRST_NAME, "female").isEmpty()
    }

    def "picks a value from the nested list matching the type"() {
        given:
            dataMaster.readResources("datamaster/base.properties")

        expect:
            dataMaster.getValuesOfType(PersonProvider.FIRST_NAME, "male", String.class) == "John"
            dataMaster.getValuesOfType(PersonProvider.FIRST_NAME, "female", String.class) == "Jane"
    }

    def "splits lists on commas"() {
        given:
            dataMaster.readResources("datamaster/base.properties")

        expect:
            dataMaster.getStringList("cities") == ["Springfield", "Shelbyville"]
    }

    def "a file replaces every root key it defines, so a flat list hides the base gendered lists"() {
        given:
            dataMaster.readResources("datamaster/base.properties")
            assert dataMaster.getValues(PersonProvider.LAST_NAME, "male") == ["Smith"]

        when:
            dataMaster.readResources("datamaster/flat-override.properties")

        then:
            dataMaster.getValues(PersonProvider.LAST_NAME, "male") == ["Kowalski", "Nowak"]
            dataMaster.getValues(PersonProvider.LAST_NAME, "female") == ["Kowalski", "Nowak"]
            dataMaster.getValues(PersonProvider.FIRST_NAME, "male") == ["John"]
    }

    def "keys are case-insensitive"() {
        given:
            dataMaster.readResources("datamaster/base.properties")

        expect:
            dataMaster.getValues("FIRSTNAMES", "MALE") == ["John"]
            dataMaster.getStringList("Cities") == ["Springfield", "Shelbyville"]
    }

    def "rejects unknown keys"() {
        given:
            dataMaster.readResources("datamaster/base.properties")

        when:
            dataMaster.getStringList("nope")

        then:
            IllegalArgumentException ex = thrown()
            ex.message == "No such key: nope"
    }

    def "explains that YAML is no longer supported when only a .yml file exists"() {
        when:
            dataMaster.readResources("datamaster/legacy.properties")

        then:
            IllegalArgumentException ex = thrown()
            ex.message.contains("datamaster/legacy.yml")
            ex.message.contains("no longer supported")
    }

    def "reports a plain missing file"() {
        when:
            dataMaster.readResources("datamaster/missing.properties")

        then:
            IllegalArgumentException ex = thrown()
            ex.message == "File datamaster/missing.properties was not found on classpath"
    }

    def "a same-named file earlier on the classpath overrides the later one"() {
        given:
            Path first = Files.createDirectory(tempDir.resolve('first'))
            Path second = Files.createDirectory(tempDir.resolve('second'))
            Files.writeString(first.resolve('custom.properties'), 'cities=Springfield\n')
            Files.writeString(second.resolve('custom.properties'), 'cities=New York,Boston\ndomains=com\n')
            URLClassLoader classLoader = new URLClassLoader([first.toUri().toURL(), second.toUri().toURL()] as URL[], (ClassLoader) null)

        when:
            dataMaster.readResources("custom.properties", classLoader)

        then:
            dataMaster.getStringList("cities") == ["Springfield"]
            dataMaster.getStringList("domains") == ["com"]

        cleanup:
            classLoader?.close()
    }

    def "can be read from many threads at once"() {
        given:
            List<String> keys = ['alphabet', 'cities', 'companyEmails', 'companyNames', 'companySuffixes', 'countries', 'domains',
                                 'firstNames.female', 'firstNames.male', 'lastNames', 'personalEmails', 'postalCodes', 'streets',
                                 'telephoneNumberFormats', 'text']
            List<MapBasedDataMaster> dataMasters = (1..20).collect {
                MapBasedDataMaster master = new MapBasedDataMaster(new BaseProducer(new RandomGenerator()))
                master.readResources("jfairy.properties")
                master.readResources("jfairy_de.properties")
                master
            }
            ExecutorService pool = Executors.newFixedThreadPool(16)

        when:
            List<Future<?>> futures = dataMasters.collectMany { master ->
                (1..16).collect { pool.submit({ keys.each { master.getStringList(it) } } as Runnable) }
            }
            futures*.get()

        then:
            noExceptionThrown()

        cleanup:
            pool.shutdownNow()
    }

    def "bundled company names of '#locale' are plain strings"() {
        given:
            dataMaster.readResources("jfairy_${locale}.properties")

        expect:
            dataMaster.getStringList('companyNames').every { it instanceof String && !it.isEmpty() }

        where:
            locale << LanguageCode.values()*.name()*.toLowerCase(Locale.ROOT)
    }
}
