package com.devskiller.jfairy.producer.util;

import com.devskiller.jfairy.producer.BaseProducer;

/**
 * Helpers for deliberately breaking check digits of generated identifiers.
 */
public final class CheckDigits {

    private static final int DECIMAL_DIGITS = 10;

    private CheckDigits() {
    }

    /**
     * Replaces the trailing decimal check digit with a different random digit.
     * <p>
     * Works for any scheme where exactly one digit value is valid in the last position,
     * so the result is guaranteed to fail checksum validation while keeping the format.
     *
     * @param number       identifier whose last character is a valid decimal check digit
     * @param baseProducer source of randomness
     * @return the same identifier with a wrong check digit
     */
    public static String replaceLastDigit(String number, BaseProducer baseProducer) {
        int lastIndex = number.length() - 1;
        int validDigit = Character.digit(number.charAt(lastIndex), DECIMAL_DIGITS);
        ValidateUtils.isTrue(validDigit >= 0, "Last character of '%s' is not a digit", number);
        int wrongDigit = (validDigit + baseProducer.randomBetween(1, DECIMAL_DIGITS - 1)) % DECIMAL_DIGITS;
        return number.substring(0, lastIndex) + wrongDigit;
    }
}
