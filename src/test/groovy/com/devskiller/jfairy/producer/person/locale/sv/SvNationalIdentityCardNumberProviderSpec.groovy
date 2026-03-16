package com.devskiller.jfairy.producer.person.locale.sv

import spock.lang.Specification

import com.devskiller.jfairy.Fairy

class SvNationalIdentityCardNumberProviderSpec extends Specification {

	private Fairy fairy
	private String nationalIdentityCardNumber

	def setup() {
		fairy = Fairy.create(Locale.forLanguageTag("sv"))
		nationalIdentityCardNumber = fairy.person().nationalIdentityCardNumber
	}

	def "should generate number with 8 characters"() {
		expect:
			nationalIdentityCardNumber.length() == 8
	}
}
