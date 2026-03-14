package com.devskiller.jfairy.producer.person.locale.pl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devskiller.jfairy.producer.person.PassportNumberProvider;
import com.devskiller.jfairy.producer.util.AlphaNumberSystem;

import static com.devskiller.jfairy.producer.util.RandomUtils.randomAlphabetic;
import static com.devskiller.jfairy.producer.util.RandomUtils.randomNumeric;

import static java.lang.Character.getNumericValue;
import static java.lang.String.valueOf;
import static java.lang.System.arraycopy;

/**
 * @author Olga Maciaszek-Sharma
 * @since 21.02.15
 */
public class PlPassportNumberProvider implements PassportNumberProvider {

	private static final int CHECKSUM_INDEX = 2;

	private static final int[] WEIGHTS = new int[]{7, 3, 9, 1, 7, 3, 1, 7, 3};

	private List<String> alphabet;

	private static Map<String, Integer> letterDigits = null;

	public PlPassportNumberProvider() {
		alphabet = AlphaNumberSystem.generateAlphabetList();
		letterDigits = generateLetterDigits();
	}

	private Map<String, Integer> generateLetterDigits() {
		Integer baseNum = 10;
		Map<String, Integer> letterDigits = new HashMap<>();
		for (String letter : alphabet) {
			letterDigits.put(letter, baseNum++);
		}
		return letterDigits;
	}


	@Override
	public String get() {
		char[] passport = new char[9];

		fillSeries(passport);

		fillDigits(passport);

		fillChecksum(passport);

		return valueOf(passport);
	}

	private void fillChecksum(char[] passport) {
		Integer checkSum = 0;

		for (int i = 0; i < 2; i++) {
			Integer checkSumValue = letterDigits.get(valueOf(passport[i]));
			checkSum += checkSumValue * WEIGHTS[i];
		}

		for (int i = 3; i < 9; i++) {
			Integer checkSumValue = getNumericValue(passport[i]);
			checkSum += checkSumValue * WEIGHTS[i];
		}

		passport[CHECKSUM_INDEX] = Integer.toString(checkSum % 10).charAt(0);

	}

	private void fillSeries(char[] passport) {
		char[] randomSeries = randomAlphabetic(2).toUpperCase().toCharArray();
		arraycopy(randomSeries, 0, passport, 0, randomSeries.length);
	}

	private void fillDigits(char[] passport) {
		char[] randomDigits = randomNumeric(6).toCharArray();
		arraycopy(randomDigits, 0, passport, 3, randomDigits.length);
	}

	public static Boolean passportCheckSumIsValid(String passportNumber) {
		char[] passport = passportNumber.toCharArray();
		int checkSum = 0;

		for (int i = 0; i < 2; i++) {
			Integer checkSumValue = letterDigits.get(valueOf(passport[i]));
			checkSum += checkSumValue * WEIGHTS[i];
		}

		for (int i = 2; i < 9; i++) {
			Integer checkSumValue = getNumericValue(passport[i]);
			checkSum += checkSumValue * WEIGHTS[i];
		}

		return checkSum % 10 == 0;

	}


}
