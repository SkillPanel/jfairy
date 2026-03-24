package com.devskiller.jfairy.producer.person.locale.pl

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class PlAddressSpec extends Specification {

	private final int SEED = 8
	private Fairy fairy
	private Address address

	def setup() {
		fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.forLanguageTag("PL")).build()
		address = fairy.person().address
	}

	def "should generate random street"() {
		expect:
			address.street == "Ludwinowska"
	}

	def "should generate random streetNumber"() {
		expect:
			address.streetNumber == "11"
	}

	def "should generate random apartmentNumber"() {
		expect:
			address.apartmentNumber == ""
	}

	def "should generate random postalCode"() {
		expect:
			address.postalCode == "25-365"
	}

	def "should generate random city"() {
		expect:
			address.city == "Milicz"
	}

	def "should return addressLine1 in pl locale format"() {
		expect:
			address.addressLine1 == "Ludwinowska 11"
	}

	def "should return addressLine2 in pl locale format"() {
		expect:
			address.addressLine2 == "25-365 Milicz"
	}

	def "should return address in pl locale format"() {
		expect:
			address.toString() == "Ludwinowska 11" + System.lineSeparator() + "25-365 Milicz"
	}

}
