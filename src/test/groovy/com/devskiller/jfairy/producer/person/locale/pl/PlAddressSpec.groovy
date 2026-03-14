package com.devskiller.jfairy.producer.person.locale.pl

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class PlAddressSpec extends Specification {

	private final int SEED = 8
	private Fairy fairy;
	private Address address

	def setup() {
		fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.forLanguageTag("PL")).build()
		address = fairy.person().address
	}

	def "should generate random street"() {
		expect:
			address.street == "Przepiórki"
	}

	def "should generate random streetNumber"() {
		expect:
			address.streetNumber == "23"
	}

    def "should generate random apartmentNumber"() {
        expect:
            address.apartmentNumber == "210"
    }

	def "should generate random postalCode"() {
		expect:
			address.postalCode == "81-260"
	}

	def "should generate random city"() {
		expect:
		    address.city == "Milicz"
	}

	def "should return addressLine1 in pl locale format"() {
		expect:
			address.addressLine1 == "Przepiórki 23, 210"
	}

	def "should return addressLine2 in pl locale format"() {
		expect:
			address.addressLine2 == "81-260 Milicz"
	}

	def "should return address in pl locale format"() {
		expect:
			address.toString() == "Przepiórki 23, 210" + System.lineSeparator() + "81-260 Milicz"
	}

}
