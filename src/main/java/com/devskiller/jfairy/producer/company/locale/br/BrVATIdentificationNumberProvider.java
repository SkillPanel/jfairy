package com.devskiller.jfairy.producer.company.locale.br;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;

/**
 * Brazilian CNPJ (Cadastro Nacional da Pessoa Jurídica) generator.
 */
public class BrVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

	private final BaseProducer baseProducer;

	public BrVATIdentificationNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		int[] digits = new int[12];
		for (int i = 0; i < 8; i++) {
			digits[i] = baseProducer.randomBetween(0, 9);
		}
		digits[8] = 0;
		digits[9] = 0;
		digits[10] = 0;
		digits[11] = 1;

		int d1 = calculateCheckDigit(digits, 12);
		int d2 = calculateCheckDigit(digits, d1, 13);

		return String.format("%d%d.%d%d%d.%d%d%d/%d%d%d%d-%d%d",
				digits[0], digits[1], digits[2], digits[3], digits[4],
				digits[5], digits[6], digits[7], digits[8], digits[9],
				digits[10], digits[11], d1, d2);
	}

	private static final int[] WEIGHTS = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

	private int calculateCheckDigit(int[] digits, int length) {
		int sum = 0;
		for (int i = 0; i < length; i++) {
			sum += digits[i] * WEIGHTS[WEIGHTS.length - length + i];
		}
		int remainder = 11 - (sum % 11);
		return remainder >= 10 ? 0 : remainder;
	}

	private int calculateCheckDigit(int[] digits, int previousDigit, int length) {
		int sum = 0;
		for (int i = 0; i < digits.length; i++) {
			sum += digits[i] * WEIGHTS[WEIGHTS.length - length + i];
		}
		sum += previousDigit * WEIGHTS[WEIGHTS.length - 1];
		int remainder = 11 - (sum % 11);
		return remainder >= 10 ? 0 : remainder;
	}
}
