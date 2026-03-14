package com.devskiller.jfairy.producer.person.locale.br

import com.devskiller.jfairy.Fairy
import spock.lang.Specification

class BrNationalIdentityCardNumberProviderSpec extends Specification {

	private Fairy fairy = Fairy.create(Locale.forLanguageTag("br"))

	def "Should generate CPF with correct format"() {
		when:
			String cpf = fairy.person().nationalIdentityCardNumber
		then:
			cpf ==~ /\d{3}\.\d{3}\.\d{3}-\d{2}/
	}

	def "Should generate CPF with 14 characters"() {
		expect:
			fairy.person().nationalIdentityCardNumber.length() == 14
		where:
			i << (1..100)
	}
}
