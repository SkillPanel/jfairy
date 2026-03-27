package com.devskiller.jfairy.producer.company.locale.tr

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator
import spock.lang.Specification

class TrVATIdentificationNumberProviderSpec extends Specification {

	def "should generate valid VKN"() {
		given:
		BaseProducer baseProducer = new BaseProducer(new RandomGenerator())
		TrVATIdentificationNumberProvider provider = new TrVATIdentificationNumberProvider(baseProducer)

		expect:
		(1..100).each {
			String vkn = provider.get()
			assert vkn.length() == 10
			int[] digits = vkn.chars().collect { Character.getNumericValue((char) it) }
			int sum = 0
			for (int i = 0; i < 9; i++) {
				int p = (digits[i] + 10 - (i + 1)) % 10
				if (p == 9) {
					sum += 9
				} else {
					int q = (int) (p * Math.pow(2, 9 - i)) % 9
					sum += q
				}
			}
			int c1 = (10 - (sum % 10)) % 10
			assert digits[9] == c1
		}
	}
}
