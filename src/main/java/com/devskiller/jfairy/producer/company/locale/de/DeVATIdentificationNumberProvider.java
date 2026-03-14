package com.devskiller.jfairy.producer.company.locale.de;

import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;

import static com.devskiller.jfairy.producer.util.RandomUtils.randomNumeric;

/**
 * German VAT identification number (Umsatzsteuer-Identifikationsnummer or USt-IdNr.)
 * <p>
 * https://en.wikipedia.org/wiki/VAT_identification_number
 *
 * @author Roland Weisleder
 */
public class DeVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

	private static final String VALID_NUMBER_PATTERN = "^[0-9]{9}$";

	@Override
	public String get() {
		return randomNumeric(9);
	}

	public boolean isValid(String vatIdentificationNumber) {
		return vatIdentificationNumber.matches(VALID_NUMBER_PATTERN);
	}

}
