package com.devskiller.jfairy

import spock.lang.Specification

import com.devskiller.jfairy.producer.person.Person
import com.devskiller.jfairy.producer.company.Company

/**
 * <p>
 * Test suite for French locale.
 * <p>
 * Note: Expected values reflect the random sequence for SEED 7 after
 * introducing Luhn-compliant VAT and specific Passport providers.
 */
class FairyFrSpec extends Specification {

	private final int SEED = 7
	private Fairy fairy = Fairy.builder()
					.withRandomSeed(SEED)
					.withLocale(Locale.FRENCH)
					.build()

	def "Should create French name"() {
		when:
			Person person = fairy.person()
		then:
			person.fullName == 'Alexis Tanguy'
	}

	def "Should create French city"() {
		when:
			Person person = fairy.person()
		then:
			person.address.city == 'Saint-Denis'
	}

	def "Should create valid French passport number"() {
			when:
					Person person = fairy.person()
			then:
					person.passportNumber ==~ /\d{2}[A-Z]{2}\d{5}/
					person.passportNumber == '28DO74569'
	}

	def "Should create valid French national identity card (CNI)"() {
			when:
					Person person = fairy.person()
			then:
					person.nationalIdentityCardNumber ==~ /88\d{2}050OLUQS/
	}

	def "Should create valid French VAT (TVA) number"() {
			given:
					Company company = fairy.company()
			when:
					String vat = company.vatIdentificationNumber
			then:
					vat == 'FR44489040287'
			and: "verify TVA key logic"
					String siren = vat.substring(4)
					int key = Integer.parseInt(vat.substring(2, 4))
					key == (12 + 3 * (Long.parseLong(siren) % 97)) % 97
	}

	def "Should satisfy Luhn algorithm for SIREN"() {
			given:
					Company company = fairy.company()
					String siren = company.vatIdentificationNumber.substring(4)
			when:
					int sum = 0
					siren.reverse().eachWithIndex { digits, i ->
							int n = Integer.parseInt(digits)
							if (i % 2 != 0) {
									n *= 2
									if (n > 9) n -= 9
							}
							sum += n
					}
			then:
					sum % 10 == 0
	}
}
