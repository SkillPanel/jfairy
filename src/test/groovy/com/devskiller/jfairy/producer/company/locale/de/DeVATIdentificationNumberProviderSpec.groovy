package com.devskiller.jfairy.producer.company.locale.de

import spock.lang.Specification
import spock.lang.Unroll
import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

/**
 * @author Roland Weisleder
 */
class DeVATIdentificationNumberProviderSpec extends Specification {

	private DeVATIdentificationNumberProvider generator = new DeVATIdentificationNumberProvider(new BaseProducer(new RandomGenerator(42)))

	@Unroll
	def "Should validate #vatIdentificationNumber as #valid"() {
		expect:
			generator.isValid(vatIdentificationNumber) == valid
		where:
			vatIdentificationNumber | valid
			'999999999'             | true
			'1234567890'            | false
			'000000000'             | true
			'18947440810'           | false
	}

	def "Should always generate proper vatIdentificationNumber"() {
		expect:
			generator.isValid(generator.get())
		where:
			i << (1..100)
	}

}
