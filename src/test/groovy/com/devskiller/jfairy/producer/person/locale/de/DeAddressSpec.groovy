package com.devskiller.jfairy.producer.person.locale.de

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class DeAddressSpec extends Specification {

	private final int SEED = 8
	private Fairy fairy
	private Address address

	def setup() {
		fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.GERMAN).build()
		address = fairy.person().address
	}

	def "should generate random street"() {
		expect:
			address.street == 'Thomas-Müntzer-Hof'
	}

	def "should generate random streetNumber"() {
		expect:
			address.streetNumber == '58'
	}

	def "should generate random apartmentNumber"() {
		expect:
			address.apartmentNumber == ''
	}

	def "should generate random postalCode"() {
		expect:
			address.postalCode == '00322'
	}

	def "should generate random city"() {
		expect:
			address.city == 'Sitzenroda'
	}

	def "should return addressLine1 in de locale format"() {
		expect:
			address.addressLine1 == 'Thomas-Müntzer-Hof 58'
	}

	def "should return addressLine2 in de locale format"() {
		expect:
			address.addressLine2 == '00322 Sitzenroda'
	}

	def "should return address in de locale format"() {
		expect:
			address.toString() == "Thomas-Müntzer-Hof 58${System.lineSeparator()}00322 Sitzenroda"
	}

}
