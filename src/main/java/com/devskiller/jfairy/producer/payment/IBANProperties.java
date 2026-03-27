package com.devskiller.jfairy.producer.payment;

import java.util.HashMap;
import java.util.Map;

import de.speedbanking.iban.IbanRegistry;

import com.devskiller.jfairy.producer.util.LanguageCode;

public final class IBANProperties {

	private static final Map<LanguageCode, IbanRegistry> COUNTRIES = new HashMap<>();

	static {
		COUNTRIES.put(LanguageCode.PL, IbanRegistry.PL);
		COUNTRIES.put(LanguageCode.EN, IbanRegistry.GB);
		COUNTRIES.put(LanguageCode.ES, IbanRegistry.ES);
		COUNTRIES.put(LanguageCode.FR, IbanRegistry.FR);
		COUNTRIES.put(LanguageCode.KA, IbanRegistry.GE);
		COUNTRIES.put(LanguageCode.IT, IbanRegistry.IT);
		COUNTRIES.put(LanguageCode.DE, IbanRegistry.DE);
		COUNTRIES.put(LanguageCode.SK, IbanRegistry.SK);
		COUNTRIES.put(LanguageCode.SV, IbanRegistry.SV);
	}

	private IBANProperties() {
	}

	public abstract static class Property {

		public abstract void apply(IBANProvider provider);

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
			.map(IbanRegistry::name)
			.findFirst()
			.orElse("PL");
	}

}
