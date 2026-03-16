/*
 * Copyright (c) 2013 Codearte and authors
 */

package com.devskiller.jfairy.producer.person.locale.es

import spock.lang.Specification

import com.devskiller.jfairy.Fairy

/**
 * @author graux
 * @since 24/06/2015
 */
class EsNationalIdentityCardNumberProviderSpec extends Specification {

	private Fairy fairy
	private String esNationalIdentityCardNumber
	private final int dniLength = 10
	private final int hyphenPos = 8

	def setup() {
		fairy = Fairy.create(Locale.forLanguageTag("es"))
		esNationalIdentityCardNumber = fairy.person().nationalIdentityCardNumber
	}

	def "should generate number with 10 characters: 8 digits, one hyphen and one letter"() {
		expect:
			esNationalIdentityCardNumber.length() == dniLength
	}

	def "should generate number divided by hyphens"() {
		given:
			char letter = esNationalIdentityCardNumber.charAt(hyphenPos)

		expect:
			letter == ('-' as char)
	}

	def "should generate number with all numbers before the hyphen"() {
		given:
			String numbers = esNationalIdentityCardNumber.substring(0, hyphenPos)

		expect:
			for (char digit : numbers.toCharArray()) {
				digit.isDigit()
			}
	}


	def "should generate number with letter after the hyphen"() {
		given:
			char letter = esNationalIdentityCardNumber.charAt(hyphenPos + 1)

		expect:
			letter.isLetter()
	}
}
