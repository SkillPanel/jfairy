package com.devskiller.jfairy.producer.person.locale.es

import spock.lang.Specification

import com.devskiller.jfairy.Fairy

/**
 * @author graux
 * @since 24/06/2015
 */
class EsPassportNumberProviderSpec extends Specification {

	private Fairy fairy
	private String passportNumber

	def setup() {
		fairy = Fairy.create(Locale.forLanguageTag("es"))
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
