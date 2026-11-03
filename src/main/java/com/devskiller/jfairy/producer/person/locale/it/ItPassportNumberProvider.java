package com.devskiller.jfairy.producer.person.locale.it;

import java.util.regex.Pattern;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

/**
 * Italian passport number (Passaporto).
 * <p>
 * <a href="https://en.wikipedia.org/wiki/Italian_passport">Italian passport</a>
 * <p>
 * E.g. YA1234567: two letters followed by seven digits.
 *
 * @since 0.10.0
 */
public class ItPassportNumberProvider implements PassportNumberProvider {

    private static final Pattern FORMAT = Pattern.compile("^[A-Z]{2}\\d{7}$");

    private final BaseProducer baseProducer;

    public ItPassportNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    /**
     * Generates a passport number.
     *
     * @return a nine character passport number
     */
    @Override
    public String get() {
        return baseProducer.letterify(baseProducer.numerify("??#######"), 'A', 'Z');
    }

    /**
     * Checks the format of a passport number.
     *
     * @param passportNumber the number to check
     * @return {@code true} if the number has two letters followed by seven digits
     */
    public boolean isValid(String passportNumber) {
        return FORMAT.matcher(passportNumber).matches();
    }
}
