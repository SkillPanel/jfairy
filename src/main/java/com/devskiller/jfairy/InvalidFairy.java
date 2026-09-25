package com.devskiller.jfairy;

import org.jspecify.annotations.Nullable;

import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.payment.IBAN;
import com.devskiller.jfairy.producer.payment.IBANFactory;
import com.devskiller.jfairy.producer.payment.IBANProperties;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties;

/**
 * Generates identifiers that look right but fail checksum validation, for negative testing.
 *
 * <p>Each value keeps the format of a valid one (length, separators, embedded birth date
 * and sex) and differs only in its check digit(s), so it exercises checksum validation
 * rather than format parsing.</p>
 *
 * <p>Methods throw {@link UnsupportedOperationException} when the current locale has
 * no checksum-based variant of the requested identifier.</p>
 *
 * <pre>{@code
 * InvalidFairy invalid = fairy.invalid();
 * String pesel = invalid.nationalIdentificationNumber();
 * Person person = fairy.person(PersonProperties.withNationalIdentificationNumber(pesel));
 * }</pre>
 */
public final class InvalidFairy {

    private final NationalIdentificationNumberFactory nationalIdentificationNumberFactory;
    private final VATIdentificationNumberProvider vatIdentificationNumberProvider;
    private final IBANFactory ibanFactory;

    InvalidFairy(NationalIdentificationNumberFactory nationalIdentificationNumberFactory,
                 VATIdentificationNumberProvider vatIdentificationNumberProvider,
                 IBANFactory ibanFactory) {
        this.nationalIdentificationNumberFactory = nationalIdentificationNumberFactory;
        this.vatIdentificationNumberProvider = vatIdentificationNumberProvider;
        this.ibanFactory = ibanFactory;
    }

    /**
     * @param properties desired number features, e.g. date of birth and sex
     * @return a national identification number with a wrong check digit
     */
    public String nationalIdentificationNumber(NationalIdentificationNumberProperties.Property... properties) {
        return nationalIdentificationNumberFactory.produceNationalIdentificationNumberProvider(properties)
            .getInvalid()
            .getValue();
    }

    /**
     * @return a VAT identification number (e.g. NIP) with a wrong check digit
     */
    public String vatIdentificationNumber() {
        return vatIdentificationNumberProvider.getInvalid();
    }

    /**
     * @param properties desired IBAN features, e.g. country
     * @return an IBAN with wrong check digits, or {@code null} if the country has no IBAN
     *         (same as {@link Fairy#iban(IBANProperties.Property...)})
     */
    public @Nullable IBAN iban(IBANProperties.Property... properties) {
        return ibanFactory.produceIBANProvider(properties).getInvalid();
    }
}
