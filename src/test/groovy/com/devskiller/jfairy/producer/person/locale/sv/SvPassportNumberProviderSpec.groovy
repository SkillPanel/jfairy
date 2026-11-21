package com.devskiller.jfairy.producer.person.locale.sv

import com.devskiller.jfairy.Fairy

import spock.lang.Specification

class SvPassportNumberProviderSpec extends Specification {

    private Fairy fairy
    private String passportNumber

    def setup() {
        fairy = Fairy.create(Locale.forLanguageTag("sv"))
        passportNumber = fairy.person().passportNumber
    }

    def "should generate number with 8 characters"() {
        expect:
            passportNumber.length() == 8
    }
}
