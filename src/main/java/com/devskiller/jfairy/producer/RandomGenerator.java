package com.devskiller.jfairy.producer;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Source of random data for all jFairy producers.
 * <p>
 * This class encapsulates a {@link Random} instance to ensure determinism
 * when a fixed seed is provided.
 */
public class RandomGenerator {

	private static final String NUMERIC_CHARS      = "0123456789";
	private static final String ALPHA_CHARS        = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHANUMERIC_CHARS = NUMERIC_CHARS + ALPHA_CHARS;

	private final Random random;

	/**
	 * Creates a new generator with a default seed.
	 */
	public RandomGenerator() {
		this.random = new Random();
	}

	/**
	 * Creates a new generator with a fixed seed for deterministic behavior.
	 *
	 * @param seed the initial seed
	 */
	public RandomGenerator(int seed) {
		this.random = new Random(seed);
	}

	/**
	 * Returns the next pseudorandom boolean value.
	 *
	 * @return a random boolean
	 */
	public boolean nextBoolean() {
		return random.nextBoolean();
	}

	/**
	 * Randomly permutes the specified list.
	 *
	 * @param <T>      the type of elements in the list
	 * @param elements the list to be shuffled
	 * @return the shuffled list
	 */
	public <T> List<T> shuffle(List<T> elements) {
		Collections.shuffle(elements, random);
		return elements;
	}

	/**
	 * Returns a random integer between min and max (inclusive).
	 *
	 * @param min lower bound (inclusive)
	 * @param max upper bound (inclusive)
	 * @return a random integer
	 */
	public int nextInt(int min, int max) {
		return min == max ? min : random.nextInt(min, max + 1);
	}

	/**
	 * Returns a random long between min and max (inclusive).
	 *
	 * @param min lower bound (inclusive)
	 * @param max upper bound (inclusive)
	 * @return a random long
	 */
	public long nextLong(long min, long max) {
		return min == max ? min : random.nextLong(min, max + 1);
	}

	/**
	 * Returns a random double between min (inclusive) and max (exclusive).
	 *
	 * @param min lower bound (inclusive)
	 * @param max upper bound (exclusive)
	 * @return a random double
	 */
	public double nextDouble(double min, double max) {
		return min == max ? min : random.nextDouble(min, max);
	}

	/**
	 * Generates a random numeric string.
	 *
	 * @param length desired length
	 * @return string of digits
	 */
	public String randomNumeric(int length) {
		return randomString(length, NUMERIC_CHARS);
	}

	/**
	 * Generates a random alphabetic string (upper-case ASCII).
	 *
	 * @param length desired length
	 * @return string of letters
	 */
	public String randomAlphabetic(int length) {
		return randomString(length, ALPHA_CHARS);
	}

	/**
	 * Generates a random alphanumeric string (upper-case ASCII).
	 *
	 * @param length desired length
	 * @return string of digits and letters
	 */
	public String randomAlphanumeric(int length) {
		return randomString(length, ALPHANUMERIC_CHARS);
	}

	private String randomString(int length, String alphabet) {
		if (length < 0) {
			throw new IllegalArgumentException("length must be >= 0, got: " + length);
		}
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
		}
		return sb.toString();
	}

}
