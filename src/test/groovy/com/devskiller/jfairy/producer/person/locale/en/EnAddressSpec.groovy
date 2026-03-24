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
			address.street == "Highland Place"
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
			address.city == "San Francisco"
	}

	def "should return addressLine1 in en locale format"() {
		expect:
			address.addressLine1 == "136 Highland Place APT 147"
	}

	def "should return addressLine2 in en locale format"() {
		expect:
			address.addressLine2 == "San Francisco 25059"
	}

	def "should return address in en locale format"() {
		expect:
			address.toString() == "136 Highland Place APT 147" + System.lineSeparator() + "San Francisco 25059"
	}

}
