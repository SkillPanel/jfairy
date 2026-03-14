package com.devskiller.jfairy.producer.person.locale.de

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class DeAddressSpec extends Specification {

	private final int SEED = 8
	private Fairy fairy;
	private Address address

	def setup() {
		fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.GERMAN).build()
		address = fairy.person().address
	}

	def "should generate random street"() {
		expect:
			address.street == 'Petrirodaer Straße'
	}

	def "should generate random streetNumber"() {
		expect:
			address.streetNumber == '184'
	}

	def "should generate random apartmentNumber"() {
		expect:
			address.apartmentNumber == '32'
	}

	def "should generate random postalCode"() {
		expect:
			address.postalCode == '60772'
	}

	def "should generate random city"() {
		expect:
			address.city == 'Zescha'
	}

	def "should return addressLine1 in de locale format"() {
		expect:
			address.addressLine1 == 'Petrirodaer Straße 184, 32'
	}

	def "should return addressLine2 in de locale format"() {
		expect:
			address.addressLine2 == '60772 Zescha'
	}

	def "should return address in de locale format"() {
		expect:
			address.toString() == "Petrirodaer Straße 184, 32${System.lineSeparator()}60772 Zescha"
	}

}
