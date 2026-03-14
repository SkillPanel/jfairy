package com.devskiller.jfairy.producer.payment;

import java.util.HashMap;
import java.util.Map;

import org.iban4j.CountryCode;

import com.devskiller.jfairy.producer.util.LanguageCode;

public final class IBANProperties {

	private final static HashMap<LanguageCode, CountryCode> COUNTRIES = new HashMap<>();

	static {
		COUNTRIES.put(LanguageCode.PL, CountryCode.PL);
		COUNTRIES.put(LanguageCode.EN, CountryCode.GB);
		COUNTRIES.put(LanguageCode.ES, CountryCode.ES);
		COUNTRIES.put(LanguageCode.FR, CountryCode.FR);
		COUNTRIES.put(LanguageCode.KA, CountryCode.GE);
		COUNTRIES.put(LanguageCode.IT, CountryCode.IT);
		COUNTRIES.put(LanguageCode.DE, CountryCode.DE);
		COUNTRIES.put(LanguageCode.SK, CountryCode.SK);
		COUNTRIES.put(LanguageCode.SV, CountryCode.SV);
		COUNTRIES.put(LanguageCode.ZH, CountryCode.TW);
	}

	private IBANProperties() {
	}

	public abstract static class Property {

		public abstract void apply(IBANProvider provider);

	}

	public static Property branchCode(final String branchCode) {
		return new Property() {
			@Override
			public void apply(IBANProvider provider) {
				provider.setBranchCode(branchCode);
			}
		};
	}

	public static Property nationalCheckDigit(final String nationalCheckDigit) {
		return new Property() {
			@Override
			public void apply(IBANProvider provider) {
				provider.setNationalCheckDigit(nationalCheckDigit);
			}
		};
	}


	public static Property accountNumber(final String accountNumber) {
		return new Property() {
			@Override
			public void apply(IBANProvider provider) {
				provider.setAccountNumber(accountNumber);
			}
		};
	}


	public static Property country(final String country) {
		return new Property() {
			@Override
			public void apply(IBANProvider provider) {
				provider.setCountry(country);
			}
		};
	}

	public static Property language(final String language) {
		return new Property() {
			@Override
			public void apply(IBANProvider provider) {
				provider.setCountry(countryFromLanguage(language));
			}
		};
	}

	private static String countryFromLanguage(String lang) {
		return COUNTRIES.entrySet().stream()
			.filter(locale -> locale.getKey().name().equals(lang))
			.map(Map.Entry::getValue)
			.map(CountryCode::getAlpha2)
			.findFirst()
			.orElse("PL");
	}

	public static Property bankCode(final String bankCode) {
		return new Property() {
			@Override
			public void apply(IBANProvider provider) {
				provider.setBankCode(bankCode);
			}
		};
	}


}
