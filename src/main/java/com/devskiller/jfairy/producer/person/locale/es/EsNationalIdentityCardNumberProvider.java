package com.devskiller.jfairy.producer.person.locale.es;

import java.util.regex.Pattern;

import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;

import static com.devskiller.jfairy.producer.util.RandomUtils.randomAlphabetic;
import static com.devskiller.jfairy.producer.util.RandomUtils.randomNumeric;

/**
 * Spanish National Identity Card Number (known as Documento Nacional de Identidad or DNI)
 *
 * @author graux
 * @since 26/04/2015
 * Documento Nacional de Identidad (DNI) Español
 */
public class EsNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

	private static final String REGEX_DNI = "^\\d{8}([-]?)[A-Z]$";
	private Pattern regexDni;

	public EsNationalIdentityCardNumberProvider() {
		this.regexDni = Pattern.compile(REGEX_DNI);
	}

	@Override
	public String get() {
		return String.format("%s-%s", randomNumeric(8), randomAlphabetic(1).toUpperCase());
	}

	public boolean isValid(String dni) {
		return this.regexDni.matcher(dni).matches();
	}
}
