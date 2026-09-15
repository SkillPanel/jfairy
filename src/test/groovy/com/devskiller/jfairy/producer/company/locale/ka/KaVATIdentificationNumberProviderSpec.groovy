package com.devskiller.jfairy.producer.company.locale.ka

import spock.lang.Specification

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

class KaVATIdentificationNumberProviderSpec extends Specification {

    private KaVATIdentificationNumberProvider provider

    def setup() {
        BaseProducer baseProducer = new BaseProducer(new RandomGenerator())
        provider = new KaVATIdentificationNumberProvider(baseProducer)
    }

    private static final Set<String> ALLOWED_PREFIXES = [ '2' ,'4' ].toSet()

    def isVatIdValid(String id) {
        return id.length() == 9 && ALLOWED_PREFIXES.contains( id[ 0..0 ] )
    }

    def "Should always generate proper VAT id number"() {
        expect:
        isVatIdValid(provider.get())
        where:
        i << (1..100)
    }
}
