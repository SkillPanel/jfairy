package com.devskiller.jfairy.producer.person.locale.sv

import com.devskiller.jfairy.Bootstrap
import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Person

import spock.lang.Specification

class SvPersonSpec extends Specification {

    private Fairy fairy = Bootstrap.create(Locale.forLanguageTag("SV"))

    def setup() {
        Bootstrap.create()
    }

    def "should create nationality"() {
        when:
            Person person = fairy.person()
        then:
            person.nationality.code == 'SE'
    }

    def "should create nationalIdentificationNumber"() {
        when:
            Person person = fairy.person()
        then:
            person.nationalIdentificationNumber
    }

}
