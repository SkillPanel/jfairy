package com.devskiller.jfairy.producer.person.locale.es

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class EsAddressSpec extends Specification {

	private final int SEED = 3
	private Fairy fairy;
	private Address address

	def setup() {
		fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.forLanguageTag("ES")).build()
		address = fairy.person().address
	}

	def "should generate random street"() {
		expect:
			address.street == "Cruz"
	}

	def "should generate random streetNumber"() {
		expect:
			address.streetNumber == "8"
	}

	def "should generate random apartmentNumber"() {
		expect:
			address.apartmentNumber == ""
	}

	def "should generate random postalCode"() {
		expect:
			address.postalCode == "40.285"
	}

	def "should generate random city"() {
		expect:
			address.city == "Alicante"
	}

	def "should return addressLine1 in es locale format"() {
		expect:
			address.addressLine1 == "Cruz, 8"
	}

	def "should return addressLine2 in es locale format"() {
		expect:
			address.addressLine2 == "40.285 Alicante"
	}

	def "should return address in es locale format"() {
		expect:
			address.toString() == "Cruz, 8" + System.lineSeparator() + "40.285 Alicante"
	}

}
