package com.devskiller.jfairy.producer.person.locale.br;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;

/**
 * Brazilian CPF (Cadastro de Pessoas Físicas) generator.
 */
public class BrNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

	private final BaseProducer baseProducer;

	public BrNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		int[] digits = new int[9];
		for (int i = 0; i < 9; i++) {
			digits[i] = baseProducer.randomBetween(0, 9);
		}

		int d1 = calculateCheckDigit(digits, 9);
		int d2 = calculateCheckDigit(digits, d1, 10);

		return String.format("%d%d%d.%d%d%d.%d%d%d-%d%d",
				digits[0], digits[1], digits[2],
				digits[3], digits[4], digits[5],
				digits[6], digits[7], digits[8],
				d1, d2);
	}

	private int calculateCheckDigit(int[] digits, int length) {
		int sum = 0;
		for (int i = 0; i < length; i++) {
			sum += digits[i] * (length + 1 - i);
		}
		int remainder = 11 - (sum % 11);
		return remainder >= 10 ? 0 : remainder;
	}

	private int calculateCheckDigit(int[] digits, int previousDigit, int length) {
		int sum = 0;
		for (int i = 0; i < digits.length; i++) {
			sum += digits[i] * (length + 1 - i);
		}
		sum += previousDigit * 2;
		int remainder = 11 - (sum % 11);
		return remainder >= 10 ? 0 : remainder;
	}
}
