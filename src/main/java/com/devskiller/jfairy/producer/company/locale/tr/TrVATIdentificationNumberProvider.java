package com.devskiller.jfairy.producer.company.locale.tr;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;

/**
 * Turkish VKN (Vergi Kimlik Numarası) generator.
 * 10 digits.
 * Last digit is a check digit.
 */
public class TrVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

	private final BaseProducer baseProducer;

	public TrVATIdentificationNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		int[] digits = new int[10];

		for (int i = 0; i < 9; i++) {
			digits[i] = baseProducer.randomBetween(0, 9);
		}

		int sum = 0;
		for (int i = 0; i < 9; i++) {
			int p = (digits[i] + 10 - (i + 1)) % 10;
			if (p == 9) {
				sum += 9;
			} else {
				int q = (int) (p * Math.pow(2, 9 - i)) % 9;
				sum += q;
			}
		}

		int c1 = (10 - (sum % 10)) % 10;
		digits[9] = c1;

		StringBuilder sb = new StringBuilder();
		for (int digit : digits) {
			sb.append(digit);
		}
		return sb.toString();
	}
}
