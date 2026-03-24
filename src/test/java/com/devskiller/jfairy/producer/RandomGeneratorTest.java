package com.devskiller.jfairy.producer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomGeneratorTest {

	private static final int SEED = 123;

	@DisplayName("Should produce identical results for same seed (Determinism)")
	@Test
	void shouldBeDeterministic() {
		RandomGenerator gen1 = new RandomGenerator(SEED);
		RandomGenerator gen2 = new RandomGenerator(SEED);

		assertEquals(gen1.randomAlphanumeric(20), gen2.randomAlphanumeric(20));
		assertEquals(gen1.nextInt(0, 100), gen2.nextInt(0, 100));
		assertEquals(gen1.randomNumeric(10), gen2.randomNumeric(10));
	}

	@DisplayName("Should generate strings of requested length")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 5, 50})
	void shouldRespectLength(int length) {
		RandomGenerator generator = new RandomGenerator();

		assertEquals(length, generator.randomNumeric(length).length());
		assertEquals(length, generator.randomAlphabetic(length).length());
		assertEquals(length, generator.randomAlphanumeric(length).length());
	}

	@DisplayName("Should contain only allowed characters")
	@Test
	void shouldContainOnlyAllowedChars() {
		RandomGenerator generator = new RandomGenerator();

		assertTrue(generator.randomNumeric(100).matches("^[0-9]+$"));
		assertTrue(generator.randomAlphabetic(100).matches("^[A-Z]+$"));
		assertTrue(generator.randomAlphanumeric(100).matches("^[A-Z0-9]+$"));
	}

	@DisplayName("Should throw exception for negative length")
	@Test
	void shouldThrowOnNegativeLength() {
		RandomGenerator generator = new RandomGenerator();

		Exception exception = assertThrows(IllegalArgumentException.class, () ->
			generator.randomNumeric(-1)
		);
		assertTrue(exception.getMessage().contains("length must be >= 0"));
	}

}
