/*
 * Copyright (c) 2013. Codearte
 */

package com.devskiller.jfairy.data

import spock.lang.Specification

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator
import com.devskiller.jfairy.producer.person.PersonProvider
import com.devskiller.jfairy.producer.util.LanguageCode

class MapBasedDataMasterSpec extends Specification {

    private MapBasedDataMaster dataMaster = new MapBasedDataMaster(new BaseProducer(new RandomGenerator()))

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

    def "bundled company names of '#locale' are plain strings"() {
        given:
            dataMaster.readResources("jfairy_${locale}.properties")

        expect:
            dataMaster.getStringList('companyNames').every { it instanceof String && !it.isEmpty() }

        where:
            locale << LanguageCode.values()*.name()*.toLowerCase(Locale.ROOT)
    }
}
