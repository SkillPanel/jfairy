package com.devskiller.jfairy.producer.company.locale.en

import spock.lang.Specification

import com.devskiller.jfairy.Fairy

/**
 * @author Olga Maciaszek-Sharma
 * @since 21.03.15
 */
class EnVATIdentificationNumberProviderSpec extends Specification {

	private Fairy fairy
	private String vatIdentificationNumber
	private final partsDividedAtChar = 2
	private final int vatIdentificationNumberLength = 10
	private final int areaPartBeginningIndex = 0
	private final int serialPartBeginningIndex = 3

	def setup() {
		fairy = Fairy.create(Locale.forLanguageTag("en"))
		vatIdentificationNumber = fairy.company().vatIdentificationNumber
	}

	def "should generate number with 10 characters"() {
		expect:
			vatIdentificationNumber.length() == vatIdentificationNumberLength
	}

	def "should generate number divided by hyphen"() {
		expect:
			vatIdentificationNumber.charAt(partsDividedAtChar) == ('-' as char)
	}

	def "should generate number with valid area part"() {
		given:
			String areaPart = vatIdentificationNumber.substring(areaPartBeginningIndex, partsDividedAtChar)
			Integer areaNumber = Integer.parseInt(areaPart)

		expect:
			for (char digit : areaPart.toCharArray()) {
				digit.isDigit()
			}
			!EnVATIdentificationNumberProvider.EXCLUDED_NUMBERS.contains(areaNumber)
	}

	def "should generate number with valid serial part"() {
		given:
			String serialPart = vatIdentificationNumber.substring(serialPartBeginningIndex, vatIdentificationNumberLength)

		expect:
			for (char digit : serialPart.toCharArray()) {
				digit.isDigit()
			}
	}

}
