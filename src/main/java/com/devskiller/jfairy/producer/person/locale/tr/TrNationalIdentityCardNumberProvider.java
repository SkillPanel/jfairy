package com.devskiller.jfairy.producer.person.locale.tr;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;

/**
 * Turkish T.C. Kimlik No generator.
 * 11 digits.
 * 1st digit cannot be 0.
 * 10th digit = ((sum(1,3,5,7,9) * 7) - sum(2,4,6,8)) % 10.
 * 11th digit = (sum(1..10)) % 10.
 */
public class TrNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

	private final BaseProducer baseProducer;

	public TrNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		int[] digits = new int[11];

		digits[0] = baseProducer.randomBetween(1, 9);
		for (int i = 1; i < 9; i++) {
			digits[i] = baseProducer.randomBetween(0, 9);
		}

		int oddSum = digits[0] + digits[2] + digits[4] + digits[6] + digits[8];
		int evenSum = digits[1] + digits[3] + digits[5] + digits[7];

		int d10 = ((oddSum * 7) - evenSum) % 10;
		if (d10 < 0) {
			d10 += 10;
		}
		digits[9] = d10;

		int totalSum = 0;
		for (int i = 0; i < 10; i++) {
			totalSum += digits[i];
		}
		digits[10] = totalSum % 10;

		StringBuilder sb = new StringBuilder();
		for (int digit : digits) {
			sb.append(digit);
		}
		return sb.toString();
	}
}
