package com.devskiller.jfairy.producer.person.locale.pl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;
import com.devskiller.jfairy.producer.util.AlphaNumberSystem;

/**
 * Provider for Polish passport numbers.
 * <p>
 * A Polish passport number consists of 9 characters: 2 uppercase letters followed by 7 digits.
 * The third character (index 2) is a checksum digit calculated based on the other positions.
 */
public class PlPassportNumberProvider implements PassportNumberProvider {

    private static final int CHECKSUM_INDEX = 2;
    private static final int PASSPORT_NUMBER_LENGTH = 9;
    private static final int RADIX_TEN = 10;

    /** Map to store numeric values for letters A-Z (A=10, B=11, etc.) */
    private static final Map<Character, Integer> LETTER_DIGITS = generateLetterDigits();

    private static final int[] WEIGHTS = {7, 3, 9, 1, 7, 3, 1, 7, 3};

    private final BaseProducer baseProducer;

    /**
     * Creates a new provider instance.
     * <p>
     * @param baseProducer the producer used for random data generation
     */
    public PlPassportNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    /**
     * Generates a mapping of characters to their numeric values for checksum calculation.
     * <p>
     * @return an unmodifiable map of characters to integers
     */
    private static Map<Character, Integer> generateLetterDigits() {
        int baseNum = 10;
        Map<Character, Integer> map = new LinkedHashMap<>();
        for (Character letter : AlphaNumberSystem.generateAlphabetList()) {
            map.put(letter, baseNum++);
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * Returns a randomly generated Polish passport number.
     * <p>
     * @return a valid 9-character passport number string
     */
    @Override
    public String get() {
        char[] passport = new char[PASSPORT_NUMBER_LENGTH];

        fillSeries(passport);
        fillDigits(passport);
        fillChecksum(passport);

        return String.valueOf(passport);
    }

    /**
     * Calculates and sets the checksum digit at the appropriate index.
     * <p>
     * @param passport the character array representing the passport number
     */
    private void fillChecksum(char[] passport) {
        // when generating, the checksum index itself must be ignored
        int checkSum = calculateTotalWeight(passport, true);
        passport[CHECKSUM_INDEX] = Character.forDigit(checkSum % RADIX_TEN, RADIX_TEN);
    }

    /**
     * Fills the first two positions with random uppercase letters.
     * <p>
     * @param passport the character array to modify
     */
    private void fillSeries(char[] passport) {
        char[] randomSeries = baseProducer.randomAlphabetic(2).toUpperCase(Locale.ROOT).toCharArray();
        System.arraycopy(randomSeries, 0, passport, 0, randomSeries.length);
    }

    /**
     * Fills the numeric positions of the passport number.
     * <p>
     * @param passport the character array to modify
     */
    private void fillDigits(char[] passport) {
        char[] randomDigits = baseProducer.randomNumeric(6).toCharArray();
        System.arraycopy(randomDigits, 0, passport, 3, randomDigits.length);
    }

    /**
     * Validates the checksum of a Polish passport number.
     * <p>
     * @param passportNumber the 9-character passport number to validate
     * @return true if the checksum is correct, false otherwise
     */
    public static boolean isPassportCheckSumValid(String passportNumber) {
        if (passportNumber == null || passportNumber.length() != PASSPORT_NUMBER_LENGTH) {
            return false;
        }
        char[] passport = passportNumber.toCharArray();
        // when validating, the checksum index (weight 9) must be included
        return calculateTotalWeight(passport, false) % RADIX_TEN == 0;
    }

    /**
     * Calculates the sum of weighted values for all characters in the passport number.
     * <p>
     * @param passport the character array of the passport
     * @param ignoreChecksumIndex whether to skip the checksum digit during calculation
     * @return the calculated weighted sum
     */
    private static int calculateTotalWeight(char[] passport, boolean ignoreChecksumIndex) {
        int totalWeight = 0;
        for (int i = 0; i < PASSPORT_NUMBER_LENGTH; i++) {
            if (ignoreChecksumIndex && i == CHECKSUM_INDEX) {
                continue;
            }

            int value;
            if (i < 2) {
                value = LETTER_DIGITS.getOrDefault(passport[i], 0);
            } else {
                // use digit for strict base-10 interpretation
                value = Character.digit(passport[i], RADIX_TEN);
            }
            totalWeight += value * WEIGHTS[i];
        }
        return totalWeight;
    }

}
