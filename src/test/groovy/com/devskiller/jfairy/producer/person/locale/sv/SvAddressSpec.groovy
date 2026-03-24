package com.devskiller.jfairy.producer.person.locale.sv

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Address

class SvAddressSpec extends Specification {

	private final int SEED = 7
	private Fairy fairy
	private Address address

	def setup() {
		fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.forLanguageTag("SV")).build()
		address = fairy.person().address
	}

	def "should generate random street"() {
		expect:
			address.street == "Styrmansgatan"
	}

	def "should generate random streetNumber"() {
		expect:
			address.streetNumber == "139"
	}

	def "should generate random apartmentNumber"() {
		expect:
			address.apartmentNumber == "321"
	}

	def "should generate random postalCode"() {
		expect:
			address.postalCode == "460 11"
	}

	def "should generate random city"() {
		expect:
			address.city == "Östhammar"
	}

	def "should return addressLine1 in sv locale format"() {
		expect:
			address.addressLine1 == "Styrmansgatan 139 Lgh 321"
	}

	def "should return addressLine2 in sv locale format"() {
		expect:
			address.addressLine2 == "460 11 Östhammar"
	}

	def "should return address in sv locale format"() {
		expect:
			address.toString() == "Styrmansgatan 139 Lgh 321" + System.lineSeparator() + "460 11 Östhammar"
	}

}
