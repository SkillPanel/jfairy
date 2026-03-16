package com.devskiller.jfairy.producer.person.locale.en

import spock.lang.Specification

import com.devskiller.jfairy.Fairy

/**
 * @author Olga Maciaszek-Sharma
 @since 15.03.15
 */
class EnPassportNumberProviderSpec extends Specification {

	private Fairy fairy
	private String passportNumber

	def setup() {
		fairy = Fairy.create(Locale.forLanguageTag("en"))
		passportNumber = fairy.person().passportNumber
	}

	def "should generate number with nine characters"() {
		expect:
			passportNumber.length() == 9
	}

	def "should contain only digits and letters"() {
		for (Character character : passportNumber) {
			expect:
				character.isDigit() || character.isLetter()
		}
	}
}
