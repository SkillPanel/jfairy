package com.devskiller.jfairy.producer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseProducer {

	private final RandomGenerator random;


	public BaseProducer(RandomGenerator random) {
		this.random = random;
	}

	/**
	 * Generates random boolean
	 *
	 * @return random boolean
	 */
	public boolean trueOrFalse() {
		return random.nextBoolean();
	}

	/**
	 * Returns random element from passed List
	 *
	 * @param <T>      element generic type
	 * @param elements list to process
	 * @return random list element
	 */
	public <T> T randomElement(List<T> elements) {
		return elements.get(randomBetween(0, elements.size() - 1));
	}

	/**
	 * Returns random element from passed vararg
	 *
	 * @param elements objects to process
	 * @return random element
	 */
	public String randomElement(String... elements) {
		return randomElement(Arrays.asList(elements));
	}

	/**
	 * Returns random enum value
	 *
	 * @param <T>      element generic type
	 * @param enumType enum class
	 * @return random enum value
	 */
	public <T extends Enum<?>> T randomElement(Class<T> enumType) {
		return enumType.getEnumConstants()[randomBetween(0, enumType.getEnumConstants().length - 1)];
	}

	/**
	 * Creates new list being random subset of the passed list
	 *
	 * @param <T>      element generic type
	 * @param elements list to process
	 * @param count    returned list size
	 * @return sublist of the elements list
	 */
	public <T> List<T> randomElements(List<T> elements, int count) {
		if (elements.size() >= count) {
			return extractRandomList(elements, count);
		} else {
			List<T> randomElements = new ArrayList<T>();
			randomElements.addAll(extractRandomList(elements, count % elements.size()));
			do {
				randomElements.addAll(extractRandomList(elements, elements.size()));
			} while (randomElements.size() < count);
			return randomElements;
		}
	}

	private <T> List<T> extractRandomList(List<T> elements, int count) {
		random.shuffle(elements);
		return elements.subList(0, count);
	}



	/**
	 * Returns random int value
	 *
	 * @param min value of the random number to be returned.  Must be positive.
	 * @param max value of the random number to be returned.  Must be positive.
	 * @return random {@code int} value between {@code min} (inclusive) and {@code max} (inclusive)
	 */
	public int randomBetween(int min, int max) {
		return random.nextInt(min, max);
	}

	/**
	 * Returns random int value
	 *
	 * @param max value of the random number to be returned.  Must be positive.
	 * @return random {@code int} value between 0 (inclusive) and {@code max} (inclusive)
	 */
	public int randomInt(int max) {
		return randomBetween(0, max);
	}

	//TODO: MZA: Duplication - should be merged into one method when consistent logic will be determined
	public char randomBetween(char min, char max) {
		return (char) randomBetween((int) min, (int) max);
	}

	/**
	 * Returns random long value from a range (including both range boundaries).
	 * It required to satisfied condition min &lt;= max.
	 *
	 * @param min lower bound of a range
	 * @param max higher bound of a range
	 * @return pseudorandom {@code long} value between {@code mon} (inclusive) and {@code max} (inclusive)
	 */
	public long randomBetween(long min, long max) {
		return random.nextLong(min, max);
	}

	/**
	 * Returns random double value
	 *
	 * @param min value of the random number to be returned.  Must be positive.
	 * @param max value of the random number to be returned.  Must be positive.
	 * @return random {@code double} value between {@code min} (inclusive) and {@code max} (inclusive)
	 */
	public double randomBetween(double min, double max) {
		return random.nextDouble(min, max);
	}

	/**
	 * Replaces all {@code '?'} characters with random chars from [a - z] range
	 *
	 * @param letterString text to process
	 * @return text with replaces {@code '?'} chars
	 */
	public String letterify(String letterString) {
		return letterify(letterString, 'a', 'z');
	}

	/**
	 * Replaces all {@code '?'} characters with random chars from [{@code from} - {@code to}] range
	 *
	 * @param letterString text to process
	 * @param from         start of the range
	 * @param to           end of the range
	 * @return text with replaced {@code '?'} chars
	 */
	public String letterify(String letterString, char from, char to) {
		return replaceSymbolWithCharsFromTo(letterString, '?', from, to);
	}

	/**
	 * Replaces all {@code '#'} characters with random numbers from [0 - 9] range
	 *
	 * @param numberString text to process
	 * @return text with replaced '#' characters
	 */
	public String numerify(String numberString) {
		return numerify(numberString, 0, 9);
	}

	/**
	 * Replaces all {@code '#'} characters with random numbers from [{@code from} - {@code to}] range
	 *
	 * @param numberString text to process
	 * @param from         start of the range
	 * @param to           end of the range
	 * @return text with replaced '#' characters
	 */
	public String numerify(String numberString, int from, int to) {
		return replaceSymbolWithCharsFromTo(numberString, '#', Character.forDigit(from, 10), Character.forDigit(to, 10));
	}

	/**
	 * Processes text with {@code numerify()} and {@code letterify()} methods
	 *
	 * @param string text to process
	 * @return text with replaced '#' and '?' characters
	 */
	public String bothify(String string) {
		return letterify(numerify(string));
	}

	private String replaceSymbolWithCharsFromTo(String string, char symbol, char from, char to) {
		StringBuilder result = new StringBuilder();
		for (char aChar : string.toCharArray()) {
			if (aChar == symbol) {
				result.append(randomBetween(from, to));
			} else {
				result.append(aChar);
			}
		}
		return result.toString();
	}
}
