package com.devskiller.jfairy.producer.person.locale.it

import spock.lang.Specification
import spock.lang.Unroll

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

class ItPassportNumberProviderSpec extends Specification {

    private BaseProducer baseProducer = new BaseProducer(new RandomGenerator())
    private ItPassportNumberProvider generator = new ItPassportNumberProvider(baseProducer)

    @Unroll
    def "Should validate #passportNumber as #valid"() {
        expect:
            generator.isValid(passportNumber) == valid
        where:
            passportNumber | valid
            'YA1234567'    | true
            'AA0000000'    | true
            'Y12345678'    | false
            'YAB123456'    | false
            'ya1234567'    | false
    }

    def "Should always generate proper passportNumber"() {
        expect:
            generator.isValid(generator.get())
        where:
            i << (1..100)
    }

}
