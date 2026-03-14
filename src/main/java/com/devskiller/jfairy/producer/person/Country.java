package com.devskiller.jfairy.producer.person;

import java.util.Arrays;
import java.util.List;
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
	Slovakia("SK", LanguageCode.SK);

	//	ISO 3166 code
	private final String code;
	// ISO 639-1
	private final List<LanguageCode> languages;

	Country(String code, LanguageCode... language) {
		this.code = code;
		this.languages = Arrays.asList(language);
	}

	public String getCode() {
		return code;
	}

	public static List<Country> findCountryForLanguage(LanguageCode language) {
		return Arrays.stream(Country.values())
			.filter(country -> country.languages.contains(language))
			.collect(Collectors.toList());
	}
}
