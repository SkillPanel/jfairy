package com.devskiller.jfairy.producer.util;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public final class AlphaNumberSystem {

	private static final char[] ALPHABET_CHARS = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
		'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};

	public static List<String> generateAlphabetList() {
		List<String> alphabetList = new ArrayList<>();
		for (char letter : ALPHABET_CHARS) {
			alphabetList.add(valueOf(letter));
		}
		return alphabetList;
	}

	private AlphaNumberSystem() {
	}

	public static String convertToString(final int numberToConvert, final int base) {
		int number = numberToConvert;
		final char[] buffer = new char[(numberToConvert / ALPHABET_CHARS.length) + 1];
		int charPosition = buffer.length - 1;

		if (number == 0) {
			buffer[charPosition--] = ALPHABET_CHARS[number];
		} else {
			while (number > 0) {
				buffer[charPosition--] = ALPHABET_CHARS[number % base];
				number /= base;
			}
		}

		return new String(buffer, charPosition + 1, buffer.length - charPosition - 1);
	}

}
