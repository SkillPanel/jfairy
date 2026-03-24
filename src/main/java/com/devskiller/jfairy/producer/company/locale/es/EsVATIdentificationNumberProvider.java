package com.devskiller.jfairy.producer.company.locale.es;

import java.util.regex.Pattern;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;

/**
 * Spanish VAT Identification Number (known as Número de Identificación Fiscal (for freelancers) or Código de Identificación Fiscal (for companies)	 in Spain)
 * <p>
 * <a href="https://en.wikipedia.org/wiki/VAT_identification_number">VAT identification number</a>
 */
public class EsVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

	private static final String REGEX_CIF = "^[A-Z][0-9]{2}[0-9]{5}([KPQSABEH]|[0-9]|[A-Z])$";

	private final BaseProducer baseProducer;

	private final Pattern regexCif;

	public EsVATIdentificationNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
		this.regexCif = Pattern.compile(REGEX_CIF);
	}

	@Override
	public String get() {
		return String.format("%s%s%s",
			baseProducer.randomAlphabetic(1).toUpperCase(),
			baseProducer.randomNumeric(7),
			baseProducer.randomAlphanumeric(1).toUpperCase());
	}

	public boolean isValid(String cif) {
		return this.regexCif.matcher(cif).matches();
	}
}
