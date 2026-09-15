package com.devskiller.jfairy.producer.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for alpha-numeric system operations.
 * <p>
 * Provides methods to handle alphabet-based numbering and conversions.
 */
public final class AlphaNumberSystem {

    private static final int ALPHABET_SIZE = 26;

    private AlphaNumberSystem() {
        throw new UnsupportedOperationException(
            "Utility class " + getClass().getSimpleName() + " cannot be instantiated");
    }

    /**
     * Generates an immutable list of all uppercase alphabet characters.
     * <p>
     * @return an unmodifiable list of characters from A to Z
     */
    public static List<Character> generateAlphabetList() {
        List<Character> alphabetList = new ArrayList<>(ALPHABET_SIZE);
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            alphabetList.add(letter);
        }
        return Collections.unmodifiableList(alphabetList);
    }

    /**
     * Converts a given number to a string representation based on the provided radix.
     * <p>
     * The conversion maps values to the uppercase alphabet (A-Z).
     *
     * @param numberToConvert the non-negative integer value to convert
     * @param base the radix to be used for conversion (1-26)
     * @return a string representation of the number
     * @throws IllegalArgumentException if numberToConvert is negative or base is out of range
     */
    public static String convertToString(final int numberToConvert, final int base) {
        if (numberToConvert < 0) {
            throw new IllegalArgumentException("Number to convert must be non-negative");
        }
        if (base <= 0 || base > ALPHABET_SIZE) {
            throw new IllegalArgumentException("Base must be between 1 and " + ALPHABET_SIZE);
        }

        // Use a safe buffer size for the conversion
        // For base 1, the length is the number itself; for others it's log based.
        int bufferLength = (base == 1) ? Math.max(1, numberToConvert) : (int) (Math.log(Math.max(1, numberToConvert)) / Math.log(base)) + 1;
        final char[] buffer = new char[bufferLength];
        int charPosition = buffer.length - 1;

        int number = numberToConvert;
        if (number == 0) {
            buffer[charPosition--] = 'A';
        } else if (base == 1) {
            for (int i = 0; i < numberToConvert; i++) {
                buffer[charPosition--] = 'A';
            }
        } else {
            while (number > 0) {
                buffer[charPosition--] = (char) ('A' + (number % base));
                number /= base;
            }
        }

        return new String(buffer, charPosition + 1, buffer.length - charPosition - 1);
    }

}
