package com.devskiller.jfairy

import com.devskiller.jfairy.producer.person.Person

import spock.lang.Specification

/**
 * @author Roland Weisleder
 */
class FairyDeSpec extends Specification {

    private final int SEED = 186
    private Fairy fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.GERMAN).build()

    def "Should create German name"() {
        when:
            Person person = fairy.person()
        then:
            person.fullName == 'Gottwaldt Höfling' // well, that's german enough...
    }

    def "Should create German city"() {
        when:
            Person person = fairy.person()
        then:
            person.address.city == 'Pyrna'
    }

}
