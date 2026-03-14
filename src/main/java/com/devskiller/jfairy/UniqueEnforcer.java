package com.devskiller.jfairy;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Wraps a generator to ensure unique values based on a key extractor.
 * Throws {@link UniqueGenerationException} after max retries.
 *
 * <pre>{@code
 * Fairy fairy = Fairy.create();
 * UniqueEnforcer<Person> unique = UniqueEnforcer.of(fairy::person, Person::getEmail);
 * Person p1 = unique.next(); // guaranteed unique email
 * Person p2 = unique.next(); // different email than p1
 * unique.reset();            // clear history
 * }</pre>
 *
 * @param <T> type of generated object
 */
public class UniqueEnforcer<T> {

	private static final int DEFAULT_MAX_RETRIES = 10_000;

	private final Supplier<T> generator;
	private final Function<T, ?> keyExtractor;
	private final int maxRetries;
	private final Set<Object> seen = new HashSet<>();

	private UniqueEnforcer(Supplier<T> generator, Function<T, ?> keyExtractor, int maxRetries) {
		this.generator = generator;
		this.keyExtractor = keyExtractor;
		this.maxRetries = maxRetries;
	}

	public static <T> UniqueEnforcer<T> of(Supplier<T> generator, Function<T, ?> keyExtractor) {
		return new UniqueEnforcer<>(generator, keyExtractor, DEFAULT_MAX_RETRIES);
	}

	public static <T> UniqueEnforcer<T> of(Supplier<T> generator, Function<T, ?> keyExtractor, int maxRetries) {
		return new UniqueEnforcer<>(generator, keyExtractor, maxRetries);
	}

	public T next() {
		for (int i = 0; i < maxRetries; i++) {
			T value = generator.get();
			Object key = keyExtractor.apply(value);
			if (seen.add(key)) {
				return value;
			}
		}
		throw new UniqueGenerationException(
				"Could not generate a unique value after " + maxRetries + " retries. "
						+ seen.size() + " unique values were generated before exhaustion.");
	}

	public void reset() {
		seen.clear();
	}

	public int size() {
		return seen.size();
	}
}
