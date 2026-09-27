package com.devskiller.jfairy.producer.payment;

import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;

/**
 * Generates {@link IBAN} instances for a country fixed via {@link #setCountry(String)}, or otherwise
 * one resolved at random from the countries speaking the current {@link com.devskiller.jfairy.data.DataMaster}
 * language.
 */
public interface IBANProvider extends Supplier<IBAN> {

    /**
     * Generates a random, valid IBAN for the country set via {@link #setCountry(String)}, or a
     * randomly resolved one if none was set.
     *
     * @return a valid {@link IBAN}, or {@code null} if the resolved country has no IBAN
     */
    @Override
    @Nullable IBAN get();

    /**
     * Generates an IBAN in the valid format but with wrong check digits, for negative testing.
     *
     * @return an IBAN that fails checksum validation, or {@code null} if the country has no IBAN
     */
    default @Nullable IBAN getInvalid() {
        throw new UnsupportedOperationException(
            "Invalid IBANs are not supported by " + getClass().getName());
    }

    /**
     * Resolves and caches the country to generate IBANs for, unless one was already set via
     * {@link #setCountry(String)}. Called automatically by {@link #get()}; call it directly only if
     * the resolved country must be known beforehand.
     */
    void fillCountryCode();

    /**
     * Fixes the country to generate IBANs for, overriding random resolution.
     *
     * @param country the ISO 3166-1 alpha-2 country code, e.g. {@code "PL"}
     */
    void setCountry(String country);

}
