package com.devskiller.jfairy.producer.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link RandomUtils}.
 */
@DisplayName("RandomUtils")
class RandomUtilsTest {

	private static final int SAMPLE_LENGTH = 20;

	@Nested
	@DisplayName("randomNumeric")
	class RandomNumeric {

		@Test
		void returnsStringOfRequestedLength() {
			assertEquals(SAMPLE_LENGTH, RandomUtils.randomNumeric(SAMPLE_LENGTH).length());
		}

		@Test
		void returnsOnlyDigits() {
			String result = RandomUtils.randomNumeric(SAMPLE_LENGTH);
			assertTrue(result.chars().allMatch(Character::isDigit),
				"Expected only digit characters, got: " + result);
		}

		@Test
		void zeroLength_returnsEmptyString() {
			assertEquals("", RandomUtils.randomNumeric(0));
		}

		@Test
		void negativeLength_throwsIllegalArgumentException() {
			assertThrows(IllegalArgumentException.class, () -> RandomUtils.randomNumeric(-1));
		}
	}

	@Nested
	@DisplayName("randomAlphabetic")
	class RandomAlphabetic {

		@Test
		void returnsStringOfRequestedLength() {
			assertEquals(SAMPLE_LENGTH, RandomUtils.randomAlphabetic(SAMPLE_LENGTH).length());
		}

		@Test
		void returnsOnlyUpperCaseLetters() {
			String result = RandomUtils.randomAlphabetic(SAMPLE_LENGTH);
			assertTrue(result.chars().allMatch(c -> c >= 'A' && c <= 'Z'),
				"Expected only A-Z characters, got: " + result);
		}

		@Test
		void zeroLength_returnsEmptyString() {
			assertEquals("", RandomUtils.randomAlphabetic(0));
		}
	}

	@Nested
	@DisplayName("randomAlphanumeric")
	class RandomAlphanumeric {

		@Test
		void returnsStringOfRequestedLength() {
			assertEquals(SAMPLE_LENGTH, RandomUtils.randomAlphanumeric(SAMPLE_LENGTH).length());
		}

		@Test
		void returnsOnlyAlphanumericChars() {
			String result = RandomUtils.randomAlphanumeric(SAMPLE_LENGTH);
			assertTrue(result.chars().allMatch(c -> Character.isDigit(c) || (c >= 'A' && c <= 'Z')),
				"Expected only 0-9 / A-Z characters, got: " + result);
		}
	}

	@Nested
	@DisplayName("randomString (package-private)")
	class RandomString {

		@ParameterizedTest(name = "length {0} → string of same length")
		@ValueSource(ints = {0, 1, 5, 100})
		void variousLengths_returnStringOfRequestedLength(int length) {
			String alphabet = "ABC123";
			String result = RandomUtils.randomString(length, alphabet);
			assertEquals(length, result.length());
			assertTrue(result.chars().allMatch(c -> alphabet.indexOf(c) >= 0));
		}

		@Test
		void emptyAlphabet_throwsIllegalArgumentException() {
			assertThrows(IllegalArgumentException.class,
				() -> RandomUtils.randomString(5, ""));
		}

		@Test
		void negativeLength_throwsIllegalArgumentException() {
			assertThrows(IllegalArgumentException.class,
				() -> RandomUtils.randomString(-1, "ABC"));
		}
	}
}
