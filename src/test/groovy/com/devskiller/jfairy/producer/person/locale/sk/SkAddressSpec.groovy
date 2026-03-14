package com.devskiller.jfairy.producer.person.locale.sk

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class SkAddressSpec extends Specification {

	private final int SEED = 7
	private Fairy fairy
	private Address address

	def setup() {
		fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.forLanguageTag("SK")).build()
		address = fairy.person().address
	}

	def "should generate street"() {
		expect:
			address.street
	}

	def "should generate streetNumber"() {
		expect:
			address.streetNumber
	}

	def "should generate postalCode"() {
		expect:
			address.postalCode
	}

	def "should generate city"() {
		expect:
			address.city
	}

	def "should return address in continental format"() {
		expect:
			address.toString().contains(System.lineSeparator())
	}

}
