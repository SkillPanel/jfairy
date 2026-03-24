package com.devskiller.jfairy.producer.company.locale.de;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;

/**
 * German VAT identification number (Umsatzsteuer-Identifikationsnummer or USt-IdNr.)
 * <p>
 * <a href="https://en.wikipedia.org/wiki/VAT_identification_number">VAT identification number</a>
 *
 * @author Roland Weisleder
 */
public class DeVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

	private static final String VALID_NUMBER_PATTERN = "^[0-9]{9}$";

	private final BaseProducer baseProducer;

	public DeVATIdentificationNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return baseProducer.randomNumeric(9);
	}

	public boolean isValid(String vatIdentificationNumber) {
		return vatIdentificationNumber.matches(VALID_NUMBER_PATTERN);
	}

}
