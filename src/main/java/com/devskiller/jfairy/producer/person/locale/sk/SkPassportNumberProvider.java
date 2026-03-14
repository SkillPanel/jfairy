package com.devskiller.jfairy.producer.person.locale.sk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;
import com.devskiller.jfairy.producer.util.AlphaNumberSystem;

import static java.lang.Character.getNumericValue;
import static java.lang.String.valueOf;
import static java.lang.System.arraycopy;

/**
 * Slovak Passport Number Provider
 */
public class SkPassportNumberProvider implements PassportNumberProvider {

	private static final int CHECKSUM_INDEX = 2;

	private static final int[] WEIGHTS = new int[]{7, 3, 9, 1, 7, 3, 1, 7, 3};

	private final List<String> alphabet;
	private final Map<String, Integer> letterDigits;
	private final BaseProducer baseProducer;

	public SkPassportNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
		alphabet = AlphaNumberSystem.generateAlphabetList();
		letterDigits = generateLetterDigits();
	}

	private Map<String, Integer> generateLetterDigits() {
		int baseNum = 10;
		Map<String, Integer> digits = new HashMap<>();
		for (String letter : alphabet) {
			digits.put(letter, baseNum++);
		}
		return digits;
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
		int checkSum = 0;

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
		char[] series = new char[2];
		for (int i = 0; i < 2; i++) {
			series[i] = (char) ('A' + baseProducer.randomInt(25));
		}
		arraycopy(series, 0, passport, 0, series.length);
	}

	private void fillDigits(char[] passport) {
		char[] digits = new char[6];
		for (int i = 0; i < 6; i++) {
			digits[i] = (char) ('0' + baseProducer.randomInt(9));
		}
		arraycopy(digits, 0, passport, 3, digits.length);
	}

	public static boolean passportCheckSumIsValid(String passportNumber) {
		List<String> alphabet = AlphaNumberSystem.generateAlphabetList();
		Map<String, Integer> digits = new HashMap<>();
		int baseNum = 10;
		for (String letter : alphabet) {
			digits.put(letter, baseNum++);
		}

		char[] passport = passportNumber.toCharArray();
		int checkSum = 0;

		for (int i = 0; i < 2; i++) {
			Integer checkSumValue = digits.get(valueOf(passport[i]));
			checkSum += checkSumValue * WEIGHTS[i];
		}

		for (int i = 2; i < 9; i++) {
			Integer checkSumValue = getNumericValue(passport[i]);
			checkSum += checkSumValue * WEIGHTS[i];
		}

		return checkSum % 10 == 0;

	}


}
