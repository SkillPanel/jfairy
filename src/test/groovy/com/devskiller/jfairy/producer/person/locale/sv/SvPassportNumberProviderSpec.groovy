package com.devskiller.jfairy.producer.person.locale.sv

import spock.lang.Specification

import com.devskiller.jfairy.Fairy

class SvPassportNumberProviderSpec extends Specification {

	private Fairy fairy
	private String passportNumber

	def setup() {
		fairy = Fairy.create(Locale.forLanguageTag("sv"))
		passportNumber = fairy.person().passportNumber
	}

	def "should generate number with 8 characters"() {
		expect:
			passportNumber.length() == 8
	}
}
