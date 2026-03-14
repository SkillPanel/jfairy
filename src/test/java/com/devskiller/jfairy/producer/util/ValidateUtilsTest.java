package com.devskiller.jfairy.producer.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit tests for {@link ValidateUtils}.
 */
@DisplayName("ValidateUtils")
class ValidateUtilsTest {

	@Nested
	@DisplayName("notNull")
	class NotNull {

		@Test
		void nonNullValue_returnsValue() {
			String value = "hello";
			assertSame(value, ValidateUtils.notNull(value, "must not be null"));
		}

		@Test
		void nullValue_throwsWithMessage() {
			IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> ValidateUtils.notNull(null, "key cannot be null"));
			assertEquals("key cannot be null", ex.getMessage());
		}

		@Test
		void nullValue_withFormatArgs_includesArgsInMessage() {
			IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> ValidateUtils.notNull(null, "param '%s' cannot be null", "myKey"));
			assertEquals("param 'myKey' cannot be null", ex.getMessage());
		}
	}

	@Nested
	@DisplayName("isTrue")
	class IsTrue {

		@Test
		void trueExpression_doesNotThrow() {
			assertDoesNotThrow(() -> ValidateUtils.isTrue(true, "should not throw"));
		}

		@Test
		void falseExpression_throwsWithMessage() {
			IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> ValidateUtils.isTrue(false, "value must be >= 0"));
			assertEquals("value must be >= 0", ex.getMessage());
		}

		@Test
		void falseExpression_withFormatArgs_includesArgsInMessage() {
			IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> ValidateUtils.isTrue(false, "%d has to be >= 0", -5));
			assertEquals("-5 has to be >= 0", ex.getMessage());
		}
	}
}
