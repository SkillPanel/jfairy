package com.devskiller.jfairy.producer.person.locale.es

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class EsAddressSpec extends Specification {

	private final int SEED = 3
	private Fairy fairy
	private Address address

	def setup() {
		fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.forLanguageTag("ES")).build()
		address = fairy.person().address
	}

	def "should generate random street"() {
		expect:
			address.street == "Gran Vía"
	}

	def "should generate random streetNumber"() {
		expect:
			address.streetNumber == "33"
	}

	def "should generate random apartmentNumber"() {
		expect:
			address.apartmentNumber == ""
	}

	def "should generate random postalCode"() {
		expect:
			address.postalCode == "52.095"
	}

	def "should generate random city"() {
		expect:
			address.city == "Jaén"
	}

	def "should return addressLine1 in es locale format"() {
		expect:
			address.addressLine1 == "Gran Vía, 33"
	}

	def "should return addressLine2 in es locale format"() {
		expect:
			address.addressLine2 == "52.095 Jaén"
	}

	def "should return address in es locale format"() {
		expect:
			address.toString() == "Gran Vía, 33" + System.lineSeparator() + "52.095 Jaén"
	}

}
