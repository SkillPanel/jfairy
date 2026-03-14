package com.devskiller.jfairy.producer.company.locale.sk

import spock.lang.Specification
import spock.lang.Unroll

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

class SkVATIdentificationNumberProviderSpec extends Specification {

	private BaseProducer baseProducer = new BaseProducer(new RandomGenerator())
	private SkVATIdentificationNumberProvider generator = new SkVATIdentificationNumberProvider(baseProducer)

	def "Should always generate proper vatIdentificationNumber"() {
		expect:
			generator.isValid(generator.get())
		where:
			i << (1..100)
	}

	def "Should generate vatIdentificationNumber with length 10"() {
		when:
			String vat = generator.get()
		then:
			vat.length() == 10
	}

	@Unroll
	def "Should validate #vatIdentificationNumber as #valid"() {

		expect:
			generator.isValid(vatIdentificationNumber) == valid

		where:
			vatIdentificationNumber | valid
			"2684494529"            | true
			"1234567890"            | false
			"0000000000"            | true
			"18947440810"           | false
	}

}
