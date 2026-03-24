package com.devskiller.jfairy.producer.company.locale.fr;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.Country;

/**
 * Provider for French VAT identification numbers (TVA intracommunautaire).
 * <p>
 * Format: FR + 2 digits (key) + 9 digits (SIREN).<br>
 * The "SIREN" satisfies the Luhn algorithm.
 *
 * @author Markus Spann
 * @since 0.8.3
 */
public class FrVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

	private final BaseProducer baseProducer;

	public FrVATIdentificationNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		String siren = generateLuhnCompliantSiren();
		int key = (12 + 3 * (Integer.parseInt(siren) % 97)) % 97;

		return Country.France.getCode() + String.format("%02d%s", key, siren);
	}

	/**
	 * Generates a 9-digit SIREN number that satisfies the Luhn algorithm.
	 *
	 * @return a valid 9-digit SIREN string
	 */
	private String generateLuhnCompliantSiren() {
		int[] digits = new int[9];
		int sum = 0;

		// generate first 8 digits in a single random call
		String base = baseProducer.randomNumeric(8);
		for (int i = 0; i < 8; i++) {
			digits[i] = Character.digit(base.charAt(i), 10);
		}

		// calculate Luhn sum for the first 8 digits
		// for a 9-digit number, doubling every second digit from the right
		// means doubling digits at indices 7, 5, 3, 1 (0-based from left).
		for (int i = 0; i < 8; i++) {
			int digit = digits[i];
			if (i % 2 != 0) {
				digit *= 2;
				if (digit > 9) {
					digit -= 9;
				}
			}
			sum += digit;
		}

		// calculate and set the check digit (9th digit)
		int checkDigit = (10 - (sum % 10)) % 10;
		digits[8] = checkDigit;

		// build the resulting string
		StringBuilder sb = new StringBuilder(9);
		for (int d : digits) {
			sb.append(d);
		}
		return sb.toString();
	}

}

