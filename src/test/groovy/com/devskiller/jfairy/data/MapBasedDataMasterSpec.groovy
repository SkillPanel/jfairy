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

    private BaseProducer baseProducer = Spy(BaseProducer, constructorArgs: [new RandomGenerator()])
    private MapBasedDataMaster data = Spy(MapBasedDataMaster, constructorArgs: [baseProducer])

    def setup() {
        baseProducer.randomBetween() >> 0
    }

    def "should read first names"() {
        when:
            DataMaster dataMaster = new MapBasedDataMaster()
            dataMaster.readResources("jfairy_en.yml")

            Map<String, List<String>> firstNames = dataMaster.getData(PersonProvider.FIRST_NAME, Map.class)
        then:
            firstNames.size() > 0
            firstNames.keySet().size() > 0
    }

    def "should return men"() {
        setup:
            data.getData(PersonProvider.FIRST_NAME, Object.class) >> [female: ['Ana', 'Ivon'], male: ['Mark']]

        when:
            String male = data.getValuesOfType(PersonProvider.FIRST_NAME, "male", String.class)

        then:
            male == "Mark"
    }

    def "should return one of women"() {
        setup:
            data.getData(PersonProvider.FIRST_NAME, Object.class) >> [female: ['Ana', 'Ivon'], male: ['Mark']]

        when:
            String female = data.getValuesOfType(PersonProvider.FIRST_NAME, "female", String.class)

        then:
            (female == "Ana") || (female == "Ivon")
    }

    def "should return an element from a flat, non-gendered list regardless of the requested type"() {
        setup:
            data.getData(PersonProvider.LAST_NAME, Object.class) >> ['Smith', 'Jones']

        when:
            String lastName = data.getValuesOfType(PersonProvider.LAST_NAME, "male", String.class)

        then:
            (lastName == "Smith") || (lastName == "Jones")
    }

    def "bundled company names of '#locale' are plain strings"() {
        given:
            MapBasedDataMaster dataMaster = new MapBasedDataMaster(new BaseProducer(new RandomGenerator()))
            dataMaster.readResources("jfairy_${locale}.yml")

        expect:
            dataMaster.getStringList('companyNames').every { it instanceof String }

        where:
            locale << LanguageCode.values()*.name()*.toLowerCase(Locale.ROOT)
    }

}
