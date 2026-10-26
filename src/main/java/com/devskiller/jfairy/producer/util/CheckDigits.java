package com.devskiller.jfairy.producer.util;

import com.devskiller.jfairy.producer.BaseProducer;

/**
 * Helpers for computing check digits of generated identifiers and for deliberately breaking them.
 */
public final class CheckDigits {

    /** Number of letters A to Z, the modulus of letter check characters. */
    public static final int LETTERS = 26;

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

    /**
     * Computes the Luhn check digit of a number.
     * <p>
     * Starting with the rightmost digit, every second digit is doubled, and 9 is subtracted from results above 9.
     * The check digit brings the sum of all digits to a multiple of 10.
     *
     * @param number the number without its check digit
     * @return the Luhn check digit
     * @throws IllegalArgumentException if the input contains non-digit characters
     */
    public static int luhnDigit(String number) {
        int sum = 0;
        boolean doubled = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.digit(number.charAt(i), DECIMAL_DIGITS);
            ValidateUtils.isTrue(n >= 0, "Character at %s of '%s' is not a digit", i, number);
            if (doubled) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            doubled = !doubled;
        }
        return (DECIMAL_DIGITS - sum % DECIMAL_DIGITS) % DECIMAL_DIGITS;
    }

    /**
     * Replaces the trailing check letter (A to Z) with a different random letter.
     *
     * @param number       identifier whose last character is a valid check letter
     * @param baseProducer source of randomness
     * @return the same identifier with a wrong check letter
     */
    public static String replaceLastLetter(String number, BaseProducer baseProducer) {
        int index = number.length() - 1;
        char validLetter = number.charAt(index);
        ValidateUtils.isTrue(validLetter >= 'A' && validLetter <= 'Z', "Character at %s of '%s' is not a letter", index, number);
        char wrongLetter = (char) ('A' + (validLetter - 'A' + baseProducer.randomBetween(1, LETTERS - 1)) % LETTERS);
        return number.substring(0, index) + wrongLetter;
    }
}
