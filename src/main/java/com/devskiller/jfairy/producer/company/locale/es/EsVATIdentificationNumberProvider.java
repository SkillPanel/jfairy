package com.devskiller.jfairy.producer.company.locale.es;

import java.util.regex.Pattern;

import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;

import static com.devskiller.jfairy.producer.util.RandomUtils.randomAlphabetic;
import static com.devskiller.jfairy.producer.util.RandomUtils.randomAlphanumeric;
import static com.devskiller.jfairy.producer.util.RandomUtils.randomNumeric;

/**
 * Spanish VAT Identification Number (known as Número de Identificación Fiscal (for freelancers) or Código de Identificación Fiscal (for companies)	 in Spain)
 * <p>
 * https://en.wikipedia.org/wiki/VAT_identification_number
 */
public class EsVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

	private static final String REGEX_CIF = "^[A-Z][0-9]{2}[0-9]{5}([KPQSABEH]|[0-9]|[A-Z])$";

	private Pattern regexCif;

	public EsVATIdentificationNumberProvider() {
		this.regexCif = Pattern.compile(REGEX_CIF);
	}

	@Override
	public String get() {
		return String.format("%s%s%s",
				randomAlphabetic(1).toUpperCase(), randomNumeric(7), randomAlphanumeric(1).toUpperCase());
	}

	public boolean isValid(String cif) {
		return this.regexCif.matcher(cif).matches();
	}
}
