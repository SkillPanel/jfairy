package com.devskiller.jfairy.producer.company.locale.se

import spock.lang.Specification

import com.devskiller.jfairy.Fairy

import static com.devskiller.jfairy.producer.company.locale.sv.SvVATIdentificationNumberProvider.isValid

class SvVATIdentificationNumberProviderSpec extends Specification {

	private static final int VAT_IDENTIFICATION_NUMBER_LENGTH = 14

	private Fairy fairy
	private String vatIdentificationNumber

	def setup() {
		fairy = Fairy.create(Locale.forLanguageTag("sv"))
		vatIdentificationNumber = fairy.company().vatIdentificationNumber
	}


	def "should generate number with 14 characters"() {
		expect:
			vatIdentificationNumber.length() == VAT_IDENTIFICATION_NUMBER_LENGTH
	}

	def "should start with SE"() {
		expect:
			vatIdentificationNumber.startsWith("SE")
	}

	def "should generate number with valid Swedish VAT Identification Number"() {
		expect:
			isValid(vatIdentificationNumber)
	}

}
