package com.devskiller.jfairy.producer.person.locale.it;

import java.util.regex.Pattern;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;

/**
 * Italian electronic identity card number (Carta d'identità elettronica, CIE).
 * <p>
 * <a href="https://en.wikipedia.org/wiki/Italian_electronic_identity_card">Italian electronic identity card</a>
 * <p>
 * E.g. CA12345BC: the letter C, a series letter, five digits and two letters.
 *
 * @since 0.10.0
 */
public class ItNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

    private static final Pattern FORMAT = Pattern.compile("^C[A-Z]\\d{5}[A-Z]{2}$");

    private final BaseProducer baseProducer;

    public ItNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    /**
     * Generates an identity card number in the current CIE format.
     *
     * @return a nine character identity card number
     */
    @Override
    public String get() {
        return baseProducer.letterify(baseProducer.numerify("C?#####??"), 'A', 'Z');
    }

    /**
     * Checks the format of an identity card number.
     *
     * @param nationalIdentityCardNumber the number to check
     * @return {@code true} if the number matches the CIE format
     */
    public boolean isValid(String nationalIdentityCardNumber) {
        return FORMAT.matcher(nationalIdentityCardNumber).matches();
    }
}
