package com.devskiller.jfairy.producer.person;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.devskiller.jfairy.producer.util.LanguageCode;

public enum Country {
	Poland("PL", LanguageCode.PL),
	UnitedKingdom("GB", LanguageCode.EN),
	Australia("AU", LanguageCode.EN),
	USA("US", LanguageCode.EN),
	Canada("CA", LanguageCode.EN, LanguageCode.FR),
	Spain("ES", LanguageCode.ES),
	France("FR", LanguageCode.FR),
	Georgia("GE", LanguageCode.KA),
	Italy("IT", LanguageCode.IT),
	Germany("DE", LanguageCode.DE),
	Sweden("SE", LanguageCode.SV),
	China("CN", LanguageCode.ZH),
	Brazil("BR", LanguageCode.BR),
	Slovakia("SK", LanguageCode.SK),
	Turkey("TR", LanguageCode.TR);

	//	ISO 3166 code
	private final String code;
	// ISO 639-1
	@SuppressWarnings("ImmutableEnumChecker")
	private final LanguageCode[] languages;

	Country(String code, LanguageCode... language) {
		this.code = code;
		this.languages = language == null ? new LanguageCode[0] : Arrays.copyOf(language, language.length);
	}

	public String getCode() {
		return code;
	}

	/**
	 * Returns the primary Java Locale for this country.
	 * <p>
	 * This method uses the first associated language code and the country's ISO code
	 * to create a {@code Locale} using the modern {@code Locale.of()} factory.
	 *
	 * @return the primary {@link java.util.Locale} for this country
	 * @throws IllegalStateException if no languages are associated with the country
	 */
	public Locale getPrimaryLocale() {
		if (languages.length == 0) {
			throw new IllegalStateException("No languages defined for country " + name());
		}
		return new Locale(languages[0].name().toLowerCase(Locale.ROOT), code);
	}

	public static List<Country> findCountryForLanguage(LanguageCode language) {
		return Arrays.stream(Country.values())
			.filter(country -> List.of(country.languages).contains(language))
			.collect(Collectors.toList());
	}
}
