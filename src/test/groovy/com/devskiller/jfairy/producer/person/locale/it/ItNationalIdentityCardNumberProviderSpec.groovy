package com.devskiller.jfairy.producer.person.locale.it

import spock.lang.Specification
import spock.lang.Unroll

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

class ItNationalIdentityCardNumberProviderSpec extends Specification {

    private BaseProducer baseProducer = new BaseProducer(new RandomGenerator())
    private ItNationalIdentityCardNumberProvider generator = new ItNationalIdentityCardNumberProvider(baseProducer)

    @Unroll
    def "Should validate #nationalIdentityCardNumber as #valid"() {
        expect:
            generator.isValid(nationalIdentityCardNumber) == valid
        where:
            nationalIdentityCardNumber | valid
            'CA12345BC'                | true
            'CZ00000AA'                | true
            'AA12345BC'                | false
            'CA1234BCD'                | false
            'ca12345bc'                | false
            'AA1234567'                | false
    }

    def "Should always generate proper nationalIdentityCardNumber"() {
        expect:
            generator.isValid(generator.get())
        where:
            i << (1..100)
    }

}
