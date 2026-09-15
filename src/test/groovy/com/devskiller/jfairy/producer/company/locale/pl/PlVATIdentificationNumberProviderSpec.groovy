package com.devskiller.jfairy.producer.company.locale.pl

import spock.lang.Specification
import spock.lang.Unroll

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

class PlVATIdentificationNumberProviderSpec extends Specification {

    private BaseProducer baseProducer = new BaseProducer(new RandomGenerator())
    private PlVATIdentificationNumberProvider generator = new PlVATIdentificationNumberProvider(baseProducer)

    @Unroll
    def "Should validate #vatIdentificationNumber as #valid"() {

        expect:
            generator.isValid(vatIdentificationNumber) == valid

        where:
            vatIdentificationNumber | valid
            "2684494529"            | true
            "1234567890"            | false
            "0000000000"            | true
            "18947440810"           | false
    }

    def "Should always generate proper vatIdentificationNumber"() {
        expect:
            generator.isValid(generator.get())
        where:
            i << (1..100)
    }

}
