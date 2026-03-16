package com.devskiller.jfairy.producer.person.locale.en

import spock.lang.Specification

import com.devskiller.jfairy.Fairy

/**
 * @author Olga Maciaszek-Sharma
 @since 15.03.15
 */
class EnNationalIdentityCardNumberProviderSpec extends Specification {

	private Fairy fairy
	private String nationalIdentityCardNumber
	private final int nationalIdentityCardNumberLength = 11
	private final int[] partsDividedAtChars = [3, 6]
	private int areaPartBeginningIndex = 0
	private final int groupPartBeginningIndex = 4
	private int serialNumberBeginningIndex = 7

	def setup() {
		fairy = Fairy.create(Locale.forLanguageTag("en"))
		nationalIdentityCardNumber = fairy.person().nationalIdentityCardNumber
	}

	def "should generate number with 11 characters"() {
		expect:
			nationalIdentityCardNumber.length() == nationalIdentityCardNumberLength
	}

	def "should generate number divided by hyphens"() {
		expect:
			for (int index : partsDividedAtChars) {
				nationalIdentityCardNumber.charAt(index) == ('-' as char)
			}
	}

	def "should generate number with valid area part"() {
		given:
			String areaPart = nationalIdentityCardNumber.substring(areaPartBeginningIndex, partsDividedAtChars[0])
			Integer areaNumber = Integer.parseInt(areaPart)
			int minAreaPartNumber = 1
			int maxAreaPartNumber = 899
			int incorrectAreaNumber = 666

		expect:
			for (char digit : areaPart.toCharArray()) {
				digit.isDigit()
			}
			areaNumber >= minAreaPartNumber && areaNumber <= maxAreaPartNumber
			areaNumber != incorrectAreaNumber
	}

	def "should generate number with valid group part"() {
		given:
			String groupPart = nationalIdentityCardNumber.substring(groupPartBeginningIndex, partsDividedAtChars[1])
			Integer groupNumber = Integer.parseInt(groupPart)
			int minGroupNumber = 1
			int maxGroupNumber = 99

		expect:
			for (char digit : groupPart.toCharArray()) {
				digit.isDigit()
			}
			groupNumber >= minGroupNumber && groupNumber <= maxGroupNumber
	}

	def "should generate number with valid serial part"() {
		given:
			String serialPart = nationalIdentityCardNumber.substring(serialNumberBeginningIndex, nationalIdentityCardNumberLength)
			Integer serialNumber = Integer.parseInt(serialPart)
			int minSerialNumber = 1
			int maxSerialNumber = 9999

		expect:
			for (char digit : serialPart.toCharArray()) {
				digit.isDigit()
			}
			serialNumber >= minSerialNumber && serialNumber <= maxSerialNumber
	}

}
