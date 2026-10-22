package com.devskiller.jfairy.producer.util;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/**
 * Supported language and country codes for data localization.
 * <p>
 * This enumeration defines the available locales for which the producers
 * can generate localized data like names, addresses, or company details.
 * <p>
 * The constant names double as the suffix of the bundled data files (e.g. {@code jfairy_br.properties}).
 * They match the ISO 639 language code, except for {@link #BR}, the Brazilian Portuguese data set,
 * whose language is {@code pt}.
 *
 * @author Olga Maciaszek-Sharma
 * @since 21.03.15
 */
public enum LanguageCode {
    PL,
    EN,
    ES,
    FR,
    KA,
    IT,
    DE,
    SV,
    ZH,
    BR("pt"),
    SK,
    TR,
    JA;

    private final String isoLanguage;

    /**
     * Creates a language code whose ISO 639 language equals its lower-cased name.
     */
    LanguageCode() {
        this.isoLanguage = name().toLowerCase(Locale.ROOT);
    }

    /**
     * Creates a language code whose ISO 639 language differs from its name.
     *
     * @param isoLanguage the ISO 639 language code
     */
    LanguageCode(String isoLanguage) {
        this.isoLanguage = isoLanguage;
    }

    /**
     * Returns the ISO 639 language code of this data set, e.g. {@code pt} for {@link #BR}.
     *
     * @return the lower-case ISO 639 language code
     * @since 0.10.0
     */
    public String getIsoLanguage() {
        return isoLanguage;
    }

    /**
     * Resolves the language code for a locale by its ISO 639 language, so {@code pt} and {@code pt-BR} map to
     * {@link #BR}. The constant name is accepted as well, which keeps the deprecated {@code br} tag working.
     *
     * @param locale the locale to resolve
     * @return the matching language code, or empty if the language is not supported
     * @since 0.10.0
     */
    public static Optional<LanguageCode> forLocale(Locale locale) {
        String language = locale.getLanguage();
        return Arrays.stream(values())
            .filter(code -> code.isoLanguage.equals(language))
            .findFirst()
            .or(() -> Arrays.stream(values())
                .filter(code -> code.name().equalsIgnoreCase(language))
                .findFirst());
    }
}
