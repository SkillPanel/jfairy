package com.devskiller.jfairy.producer.person.locale.pl

import spock.lang.Specification

import com.devskiller.jfairy.Fairy

/**
 * @author Olga Maciaszek-Sharma
 @since 21.02.15
 */
class PlPassportNumberProviderSpec extends Specification {

    private Fairy fairy
    private String passportNumber

    def setup() {
        fairy = Fairy.create(Locale.forLanguageTag("pl"))
        passportNumber = fairy.person().passportNumber
    }

    def "should generate number with correct length"() {
        expect:
            passportNumber.length() == 9
    }

    def "should generate number starting with series"() {
        expect:
            passportNumber.toCharArray()[0..1].every { Character.isLetter(it) }
    }

    def "should generate number ending with 6 digits"() {
        expect:
            passportNumber.toCharArray()[3..8].every { Character.isDigit(it) }
    }

    def "should generate number with correct checksum"() {
        expect:
            PlPassportNumberProvider.isPassportCheckSumValid(passportNumber)
    }

}
