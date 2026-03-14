package com.devskiller.jfairy.producer;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomGenerator {

	private final Random random;

	public RandomGenerator() {
		this.random = new Random();
	}

	public RandomGenerator(int seed) {
		this.random = new Random(seed);
	}

	public boolean nextBoolean() {
		return random.nextBoolean();
	}

	public <T> List<T> shuffle(List<T> elements) {
		Collections.shuffle(elements, random);
		return elements;
	}

	public int nextInt(int min, int max) {
		if (min == max) return min;
		return min + random.nextInt(max - min + 1);
	}

	public long nextDouble(long min, long max) {
		if (min == max) return min;
		long range = max - min + 1;
		if (range <= 0) {
			// Overflow: rejection sampling
			while (true) {
				long val = random.nextLong();
				if (val >= min && val <= max) {
					return val;
				}
			}
		} else if (range <= Integer.MAX_VALUE) {
			return min + (long) random.nextInt((int) range);
		} else {
			// Large range: use nextBytes-based algorithm matching commons-math3
			return min + nextLongFromBytes(range);
		}
	}

	/**
	 * Generates a random long in [0, n) using nextBytes(8),
	 * matching the commons-math3 RandomDataGenerator.nextLong(RandomGenerator, long) algorithm.
	 */
	private long nextLongFromBytes(long n) {
		byte[] bytes = new byte[8];
		while (true) {
			random.nextBytes(bytes);
			long val = 0;
			for (byte b : bytes) {
				val = (val << 8) | (b & 0xFFL);
			}
			val = val & Long.MAX_VALUE;
			long result = val % n;
			if (val - result + (n - 1) >= 0) {
				return result;
			}
		}
	}

	public double nextDouble(double min, double max) {
		double u = random.nextDouble();
		while (u <= 0.0) {
			u = random.nextDouble();
		}
		return u * max + (1.0 - u) * min;
	}
}
