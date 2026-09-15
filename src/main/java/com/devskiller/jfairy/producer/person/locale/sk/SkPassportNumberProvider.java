package com.devskiller.jfairy.producer.person.locale.sk;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;
import com.devskiller.jfairy.producer.util.AlphaNumberSystem;

/**
 * Slovak Passport Number Provider.
 * <p>
 * The passport number consists of 9 characters: 2 letters, 1 checksum digit, and 6 digits.
 */
public class SkPassportNumberProvider implements PassportNumberProvider {

    private static final int CHECKSUM_INDEX = 2;
    private static final int PASSPORT_NUMBER_LENGTH = 9;
    private static final int RADIX_TEN = 10;

    private static final int[] WEIGHTS = {7, 3, 9, 1, 7, 3, 1, 7, 3};

    /** Map to store numeric values for letters A-Z (A=10, B=11, etc.) */
    private static final Map<Character, Integer> LETTER_DIGITS = generateLetterDigits();

    private final BaseProducer baseProducer;

    public SkPassportNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    private static Map<Character, Integer> generateLetterDigits() {
        int baseNum = 10;
        List<Character> alphabet = AlphaNumberSystem.generateAlphabetList();
        Map<Character, Integer> map = new LinkedHashMap<>(alphabet.size());
        for (Character letter : alphabet) {
            map.put(letter, baseNum++);
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public String get() {
        char[] passport = new char[PASSPORT_NUMBER_LENGTH];

        fillSeries(passport);

        fillDigits(passport);

        fillChecksum(passport);

        return String.valueOf(passport);
    }

    private void fillChecksum(char[] passport) {
        int checkSum = calculateTotalWeight(passport);
        // the checksum digit itself is the remainder mod 10
        passport[CHECKSUM_INDEX] = Character.forDigit(checkSum % RADIX_TEN, RADIX_TEN);
    }

    private void fillSeries(char[] passport) {
        for (int i = 0; i < 2; i++) {
            passport[i] = (char) ('A' + baseProducer.randomInt(26));
        }
    }

    private void fillDigits(char[] passport) {
        for (int i = 3; i < PASSPORT_NUMBER_LENGTH; i++) {
            passport[i] = (char) ('0' + baseProducer.randomInt(RADIX_TEN));
        }
    }

    /**
     * Validates the checksum of a Slovak passport number
     * <p>
     * @param passportNumber the 9-character passport number string
     * @return true if the checksum is valid
     */
    public static boolean isPassportCheckSumValid(String passportNumber) {
        if (passportNumber == null || passportNumber.length() != PASSPORT_NUMBER_LENGTH) {
            return false;
        }

        char[] passport = passportNumber.toCharArray();
        return calculateTotalWeight(passport) % RADIX_TEN == 0;
    }

    private static int calculateTotalWeight(char[] passport) {
        int totalWeight = 0;
        for (int i = 0; i < PASSPORT_NUMBER_LENGTH; i++) {
            if (i == CHECKSUM_INDEX) {
                continue;
            }

            int value;
            if (i < 2) {
                value = LETTER_DIGITS.getOrDefault(passport[i], 0);
            } else {
                // errorprone: using digit(c, 10) for strict decimal interpretation
                value = Character.digit(passport[i], RADIX_TEN);
            }
            totalWeight += value * WEIGHTS[i];
        }
        return totalWeight;
    }

}
