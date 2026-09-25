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
     *
     * @param number       identifier whose last character is a valid decimal check digit
     * @param baseProducer source of randomness
     * @return the same identifier with a wrong check digit
     * @see #replaceDigitAt(String, int, BaseProducer)
     */
    public static String replaceLastDigit(String number, BaseProducer baseProducer) {
        return replaceDigitAt(number, number.length() - 1, baseProducer);
    }

    /**
     * Replaces the decimal check digit at the given position with a different random digit.
     * <p>
     * Works for any scheme where exactly one digit value is valid in that position,
     * so the result is guaranteed to fail checksum validation while keeping the format.
     *
     * @param number       identifier containing a valid decimal check digit
     * @param index        position of the check digit
     * @param baseProducer source of randomness
     * @return the same identifier with a wrong check digit
     */
    public static String replaceDigitAt(String number, int index, BaseProducer baseProducer) {
        int validDigit = Character.digit(number.charAt(index), DECIMAL_DIGITS);
        ValidateUtils.isTrue(validDigit >= 0, "Character at %s of '%s' is not a digit", index, number);
        int wrongDigit = (validDigit + baseProducer.randomBetween(1, DECIMAL_DIGITS - 1)) % DECIMAL_DIGITS;
        return number.substring(0, index) + wrongDigit + number.substring(index + 1);
    }
}
