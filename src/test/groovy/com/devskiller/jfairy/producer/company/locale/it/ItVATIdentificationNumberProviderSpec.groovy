package com.devskiller.jfairy.producer.company.locale.it

import spock.lang.Specification
import spock.lang.Unroll

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

class ItVATIdentificationNumberProviderSpec extends Specification {

    private ItVATIdentificationNumberProvider provider = new ItVATIdentificationNumberProvider(new BaseProducer(new RandomGenerator()))

    @Unroll
    def "should validate #vatIdentificationNumber as #valid"() {
        expect:
            ItVATIdentificationNumberProvider.isValid(vatIdentificationNumber) == valid

        where:
            vatIdentificationNumber | valid
            "12345678903"           | true
            "00000000000"           | true
            "12345678904"           | false
            "1234567890"            | false
            "123456789031"          | false
            "1234567890A"           | false
    }

    def "should always generate valid VAT identification numbers"() {
        when:
            def numbers = (1..200).collect { provider.get() }
        then:
            numbers.every { ItVATIdentificationNumberProvider.isValid(it) }
            numbers.every { (1..100).contains(Integer.parseInt(it.substring(7, 10))) }
    }
}
