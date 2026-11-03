package com.devskiller.jfairy.producer.person.locale.en

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class EnAddressSpec extends Specification {

    private final int SEED = 7
    private Fairy fairy
    private Address address

    def setup() {
        fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.forLanguageTag("EN")).build()
        address = fairy.person().address
    }

    def "should generate random street"() {
        expect:
            address.street == "Birch Terrace"
    }

    def "should generate random streetNumber"() {
        expect:
            address.streetNumber == "136"
    }

    def "should generate random apartmentNumber"() {
        expect:
            address.apartmentNumber == "147"
    }

    def "should generate random postalCode"() {
        expect:
            address.postalCode == "25059"
    }

    def "should generate random city"() {
        expect:
            address.city == "Boise"
    }

    def "should return addressLine1 in en locale format"() {
        expect:
            address.addressLine1 == "136 Birch Terrace APT 147"
    }

    def "should return addressLine2 in en locale format"() {
        expect:
            address.addressLine2 == "Boise 25059"
    }

    def "should return address in en locale format"() {
        expect:
            address.toString() == "136 Birch Terrace APT 147" + System.lineSeparator() + "Boise 25059"
    }

}
