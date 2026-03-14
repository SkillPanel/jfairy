package com.devskiller.jfairy.producer.util;

import java.util.random.RandomGenerator;

/**
 * Utility class for generating random strings.
 *
 * <p>Provides simple factory methods for the three character sets most
 * commonly needed by locale-specific providers:
 * <ul>
 *   <li>numeric ({@code 0–9})</li>
 *   <li>alphabetic ({@code A–Z}, upper-case only)</li>
 *   <li>alphanumeric ({@code 0–9}, {@code A–Z}, upper-case only)</li>
 * </ul>
 *
 * <p>All methods use {@link RandomGenerator#getDefault()} which delegates to
 * the JVM's default random source (typically {@code L64X128MixRandom} on Java 17+).
 * This is intentional: the callers that previously used
 * {@code org.apache.commons.lang3.RandomStringUtils} also relied on an
 * unseeded, thread-local random source and did not require reproducibility.
 * Where reproducibility from a fixed seed is required, use
 * {@link com.devskiller.jfairy.producer.BaseProducer} directly.
 *
 * @author Markus Spann
 * @since 2026/03/14
 */
public final class RandomUtils {

	private static final String NUMERIC_CHARS = "0123456789";
	private static final String ALPHA_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHANUMERIC_CHARS = NUMERIC_CHARS + ALPHA_CHARS;

	private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

	/** Prevent instantiation of this utility class. */
	private RandomUtils() {
		throw new UnsupportedOperationException(
			"Utility class " + getClass().getSimpleName() + " cannot be instantiated");
	}

	/**
	 * Generates a random string of the given length containing only digit characters
	 * ({@code 0–9}).
	 *
	 * @param length the desired string length; must be &gt;= 0
	 * @return a random numeric string of the requested length
	 * @throws IllegalArgumentException if {@code length} is negative
	 */
	public static String randomNumeric(int length) {
		return randomString(length, NUMERIC_CHARS);
	}

	/**
	 * Generates a random string of the given length containing only upper-case
	 * ASCII letters ({@code A–Z}).
	 *
	 * @param length the desired string length; must be &gt;= 0
	 * @return a random alphabetic string of the requested length
	 * @throws IllegalArgumentException if {@code length} is negative
	 */
	public static String randomAlphabetic(int length) {
		return randomString(length, ALPHA_CHARS);
	}

	/**
	 * Generates a random string of the given length containing upper-case ASCII
	 * letters ({@code A–Z}) and digits ({@code 0–9}).
	 *
	 * @param length the desired string length; must be &gt;= 0
	 * @return a random alphanumeric string of the requested length
	 * @throws IllegalArgumentException if {@code length} is negative
	 */
	public static String randomAlphanumeric(int length) {
		return randomString(length, ALPHANUMERIC_CHARS);
	}

	/**
	 * Generates a random string of the given length by sampling characters
	 * uniformly at random from {@code alphabet}.
	 *
	 * @param length   the desired string length, must be &gt;= 0
	 * @param alphabet the pool of characters to draw from; must not be empty
	 * @return a random string drawn from {@code alphabet}
	 * @throws IllegalArgumentException if {@code length} is negative or
	 *                                  {@code alphabet} is empty
	 */
	static String randomString(int length, String alphabet) {
		ValidateUtils.isTrue(length >= 0, "length must be >= 0, got: %d", length);
		ValidateUtils.isTrue(!alphabet.isEmpty(), "alphabet must not be empty");
		StringBuilder sb = new StringBuilder(length);
		int bound = alphabet.length();
		for (int i = 0; i < length; i++) {
			sb.append(alphabet.charAt(RANDOM.nextInt(bound)));
		}
		return sb.toString();
	}
}
