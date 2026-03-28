package com.devskiller.jfairy.producer.person.locale.tr

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator
import spock.lang.Specification

class TrNationalIdentityCardNumberProviderSpec extends Specification {

	def "should generate valid T.C. Kimlik No"() {
		given:
		BaseProducer baseProducer = Mock()
		TrNationalIdentityCardNumberProvider provider = new TrNationalIdentityCardNumberProvider(baseProducer)

		when:
		baseProducer.randomBetween(1, 9) >> 1
		baseProducer.randomBetween(0, 9) >>> [2, 3, 4, 5, 6, 7, 8, 9]
		String tckn = provider.get()

		then:
		tckn.length() == 11
		tckn.startsWith("1")
		// 1 2 3 4 5 6 7 8 9
		// oddSum = 1+3+5+7+9 = 25
		// evenSum = 2+4+6+8 = 20
		// d10 = (25 * 7 - 20) % 10 = (175 - 20) % 10 = 155 % 10 = 5
		// totalSum = 1+2+3+4+5+6+7+8+9+5 = 50
		// d11 = 50 % 10 = 0
		tckn == "12345678950"
	}

	def "should generate multiple valid T.C. Kimlik Nos"() {
		given:
		BaseProducer baseProducer = new BaseProducer(new RandomGenerator())
		TrNationalIdentityCardNumberProvider provider = new TrNationalIdentityCardNumberProvider(baseProducer)

		expect:
		(1..100).each {
			String tckn = provider.get()
			assert tckn.length() == 11
			assert !tckn.startsWith("0")
			int[] digits = tckn.chars().collect { Character.getNumericValue((char) it) }
			int oddSum = digits[0] + digits[2] + digits[4] + digits[6] + digits[8]
			int evenSum = digits[1] + digits[3] + digits[5] + digits[7]
			int d10 = ((oddSum * 7) - evenSum) % 10
			if (d10 < 0) d10 += 10
			assert digits[9] == d10
			assert digits[10] == (digits[0..9].sum() % 10)
		}
	}
}
