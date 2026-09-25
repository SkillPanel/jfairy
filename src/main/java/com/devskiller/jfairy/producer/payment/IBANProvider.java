package com.devskiller.jfairy.producer.payment;

import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;

public interface IBANProvider extends Supplier<IBAN> {

    @Override
    IBAN get();

    /**
     * Generates an IBAN in the valid format but with wrong check digits, for negative testing.
     *
     * @return an IBAN that fails checksum validation, or {@code null} if the country has no IBAN
     */
    default @Nullable IBAN getInvalid() {
        throw new UnsupportedOperationException(
            "Invalid IBANs are not supported by " + getClass().getSimpleName());
    }

    void fillCountryCode();

    void setCountry(String country);

}
