package com.devskiller.jfairy.producer.person.locale.pl

import spock.lang.Specification

import com.devskiller.jfairy.Bootstrap
import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Person

class PlPersonSpec extends Specification {

    private Fairy fairy = Bootstrap.create(Locale.forLanguageTag("PL"))

    def setup() {
        Bootstrap.create()
    }

    def "should create nationality"() {
        when:
            Person person = fairy.person()
        then:
            person.nationality.code == 'PL'
    }

    def "should create nationalIdentificationNumber"() {
        when:
            Person person = fairy.person()
        then:
            person.nationalIdentificationNumber
    }

}
