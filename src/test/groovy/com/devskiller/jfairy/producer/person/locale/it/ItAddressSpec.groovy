package com.devskiller.jfairy.producer.person.locale.it

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class ItAddressSpec extends Specification {

    private final int SEED = 8
    private Fairy fairy
    private Address address

    def setup() {
        fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.ITALIAN).build()
        address = fairy.person().address
    }

    def "should generate random street"() {
        expect:
            address.street == 'Via Camillo Cavour'
    }

    def "should generate random streetNumber"() {
        expect:
            address.streetNumber == '74'
    }

    def "should generate random apartmentNumber"() {
        expect:
            address.apartmentNumber == ''
    }

    def "should generate random postalCode"() {
        expect:
            address.postalCode == '32209'
    }

    def "should generate random city"() {
        expect:
            address.city == 'Reggio Emilia'
    }

    def "should return addressLine1 in it locale format"() {
        expect:
            address.addressLine1 == 'Via Camillo Cavour, 74'
    }

    def "should return addressLine2 in it locale format"() {
        expect:
            address.addressLine2 == '32209 Reggio Emilia'
    }

    def "should return address in it locale format"() {
        expect:
            address.toString() == "Via Camillo Cavour, 74${System.lineSeparator()}32209 Reggio Emilia"
    }

    def "should mark the apartment number as interno"() {
        given:
            Address withApartment = new ItAddress('Via Roma', '12', '5', '00184', 'Roma')
        expect:
            withApartment.addressLine1 == 'Via Roma, 12, int. 5'
    }

}
