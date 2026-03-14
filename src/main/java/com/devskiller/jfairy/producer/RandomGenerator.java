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
		return random.nextInt(min, max + 1);
	}

	public long nextLong(long min, long max) {
		if (min == max) return min;
		return random.nextLong(min, max + 1);
	}

	public double nextDouble(double min, double max) {
		if (min == max) return min;
		return random.nextDouble(min, max);
	}
}
