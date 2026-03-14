package com.devskiller.jfairy.producer.company.locale.br

import com.devskiller.jfairy.Bootstrap
import com.devskiller.jfairy.Fairy
import spock.lang.Specification

class BrVATIdentificationNumberProviderSpec extends Specification {

	private Fairy fairy = Fairy.create(Locale.forLanguageTag("br"))

	def "Should generate CNPJ with correct format"() {
		when:
			String cnpj = fairy.company().vatIdentificationNumber
		then:
			cnpj ==~ /\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}/
	}

	def "Should always generate CNPJ with 18 characters"() {
		expect:
			fairy.company().vatIdentificationNumber.length() == 18
		where:
			i << (1..100)
	}
}
