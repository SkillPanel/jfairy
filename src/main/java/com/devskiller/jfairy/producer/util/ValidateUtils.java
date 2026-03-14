package com.devskiller.jfairy.producer.util;

import org.jspecify.annotations.Nullable;

/**
 * Utility class providing argument-validation helpers.
 *
 * <p>Methods throw {@link IllegalArgumentException} on failure.
 *
 * @author Markus Spann
 * @since 2026/03/14
 */
public final class ValidateUtils {

	/** Prevent instantiation of this utility class. */
	private ValidateUtils() {
		throw new UnsupportedOperationException(
			"Utility class " + getClass().getSimpleName() + " cannot be instantiated");
	}

	/**
	 * Validates that {@code value} is not {@code null}.
	 *
	 * @param <T>     the type of the object being validated
	 * @param value   the value to check
	 * @param message the detail message used in the exception; may contain
	 *                {@link String#format} placeholders
	 * @param args    optional arguments for {@code message}
	 * @return {@code value}, guaranteed non-null, for convenient inline use
	 * @throws IllegalArgumentException if {@code value} is {@code null}
	 */
	public static <T> T notNull(@Nullable T value, String message, Object... args) {
		if (value == null) {
			throw new IllegalArgumentException(
				args.length == 0 ? message : String.format(message, args));
		}
		return value;
	}

	/**
	 * Validates that {@code expression} is {@code true}.
	 *
	 * @param expression the boolean expression to check
	 * @param message    the detail message used in the exception; may contain
	 *                   {@link String#format} placeholders
	 * @param args       optional arguments for {@code message}
	 * @throws IllegalArgumentException if {@code expression} is {@code false}
	 */
	public static void isTrue(boolean expression, String message, Object... args) {
		if (!expression) {
			throw new IllegalArgumentException(
				args.length == 0 ? message : String.format(message, args));
		}
	}
}
