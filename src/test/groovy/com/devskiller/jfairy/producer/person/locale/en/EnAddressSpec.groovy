package com.devskiller.jfairy.producer.person.locale.en

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class EnAddressSpec extends Specification {

	private final int SEED = 7
	private Fairy fairy;
	private Address address

	def setup() {
		fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.forLanguageTag("EN")).build()
		address = fairy.person().address
	}

	def "should generate random street"() {
		expect:
			address.street == "Herzi Street"
	}

	def "should generate random streetNumber"() {
		expect:
			address.streetNumber == "63"
	}

    def "should generate random apartmentNumber"() {
        expect:
            address.apartmentNumber == ""
    }

	def "should generate random postalCode"() {
		expect:
			address.postalCode == "42856"
	}

	def "should generate random city"() {
		expect:
		    address.city == "Washington"
	}

	def "should return addressLine1 in en locale format"() {
		expect:
			address.addressLine1 == "63 Herzi Street"
	}

	def "should return addressLine2 in en locale format"() {
		expect:
			address.addressLine2 == "Washington 42856"
	}

	def "should return address in en locale format"() {
		expect:
            address.toString() == "63 Herzi Street" + System.lineSeparator() + "Washington 42856"
	}

}
