package com.devskiller.jfairy.producer.util;

import java.text.Normalizer;
import java.util.List;

import org.jspecify.annotations.Nullable;

/**
 * Utility class providing string manipulation helpers used throughout jFairy.
 *
 * <p>Methods are intentionally minimal: only functionality actually required by
 * the library is implemented. The API deliberately mirrors the subset of
 * {@code org.apache.commons.lang3.StringUtils} that was in use, so migration
 * of call-sites is mechanical.
 *
 * <p>Null-handling contract: every method that accepts a {@link String}
 * parameter documents its null-behavior explicitly. Methods annotated
 * {@code @Nullable} on the parameter accept {@code null}; all others require
 * non-null input and will throw {@link NullPointerException} if passed
 * {@code null}.
 *
 * @author Markus Spann
 * @since 2026/03/14
 */
public final class StringUtils {

	/** Prevent instantiation of this utility class. */
	private StringUtils() {
		throw new UnsupportedOperationException(
			"Utility class " + getClass().getSimpleName() + " cannot be instantiated");
	}

	// -------------------------------------------------------------------------
	// Blank / empty checks
	// -------------------------------------------------------------------------

	/**
	 * Returns {@code true} when the string is neither {@code null}, nor empty,
	 * nor contains only whitespace characters.
	 *
	 * @param s the string to check; may be {@code null}
	 * @return {@code true} if {@code s} has at least one non-whitespace character
	 */
	public static boolean isNotBlank(@Nullable String s) {
		return s != null && !s.isBlank();
	}

	/**
	 * Returns {@code true} when the string is neither {@code null} nor empty
	 * (length zero). A string containing only whitespace is considered
	 * <em>not empty</em>.
	 *
	 * @param s the string to check; may be {@code null}
	 * @return {@code true} if {@code s} is non-null and has length &gt; 0
	 */
	public static boolean isNotEmpty(@Nullable String s) {
		return s != null && !s.isEmpty();
	}

	// -------------------------------------------------------------------------
	// Case conversion
	// -------------------------------------------------------------------------

	/**
	 * Converts all characters in {@code s} to lower case using the rules of the
	 * default locale.
	 *
	 * @param s the string to convert; must not be {@code null}
	 * @return lower-case version of {@code s}
	 */
	public static String lowerCase(String s) {
		return s.toLowerCase();
	}

	/**
	 * Capitalizes the first character of {@code s} (converts it to upper case)
	 * and leaves the rest unchanged.
	 *
	 * @param s the string to capitalize; must not be {@code null}
	 * @return the string with its first character in upper case, or {@code s}
	 *         unchanged if it is empty
	 */
	public static String capitalize(String s) {
		if (s.isEmpty()) {
			return s;
		}
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	/**
	 * Un-capitalizes the first character of {@code s} (converts it to lower
	 * case) and leaves the rest unchanged.
	 *
	 * @param s the string to un-capitalize; must not be {@code null}
	 * @return the string with its first character in lower case, or {@code s}
	 *         unchanged if it is empty
	 */
	public static String uncapitalize(String s) {
		if (s.isEmpty()) {
			return s;
		}
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	// -------------------------------------------------------------------------
	// Padding
	// -------------------------------------------------------------------------

	/**
	 * Left-pads {@code s} with the character {@code padChar} until its length
	 * reaches {@code size}. If {@code s} is already at least {@code size}
	 * characters long, it is returned unchanged.
	 *
	 * @param s       the string to pad; must not be {@code null}
	 * @param size    the desired minimum length
	 * @param padChar the padding character
	 * @return the padded string
	 */
	public static String leftPad(String s, int size, char padChar) {
		int pads = size - s.length();
		if (pads <= 0) {
			return s;
		}
		return String.valueOf(padChar).repeat(pads) + s;
	}

	/**
	 * Left-pads {@code s} with the first character of {@code padStr} until its
	 * length reaches {@code size}. If {@code s} is already at least {@code size}
	 * characters long, it is returned unchanged.
	 *
	 * @param s      the string to pad; must not be {@code null}
	 * @param size   the desired minimum length
	 * @param padStr the padding string (only the first character is used);
	 *               must not be {@code null} or empty
	 * @return the padded string
	 */
	public static String leftPad(String s, int size, String padStr) {
		return leftPad(s, size, padStr.charAt(0));
	}

	// -------------------------------------------------------------------------
	// Trimming / stripping
	// -------------------------------------------------------------------------

	/**
	 * Removes leading and trailing occurrences of any character in
	 * {@code stripChars} from {@code s}.
	 *
	 * <p>Example: {@code strip("...hello...", ".")} returns {@code "hello"}.
	 *
	 * @param s          the string to strip; must not be {@code null}
	 * @param stripChars the characters to strip; must not be {@code null}
	 * @return the stripped string
	 */
	public static String strip(String s, String stripChars) {
		int start = 0;
		int end = s.length();
		while (start < end && stripChars.indexOf(s.charAt(start)) >= 0) {
			start++;
		}
		while (end > start && stripChars.indexOf(s.charAt(end - 1)) >= 0) {
			end--;
		}
		return s.substring(start, end);
	}

	/**
	 * Removes all whitespace characters from {@code s}.
	 *
	 * <p>Whitespace is defined by {@link Character#isWhitespace(int)}.
	 *
	 * @param s the string; must not be {@code null}
	 * @return the string with all whitespace removed
	 */
	public static String deleteWhitespace(String s) {
		int len = s.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (!Character.isWhitespace(c)) {
				sb.append(c);
			}
		}
		return sb.length() == len ? s : sb.toString();
	}

	// -------------------------------------------------------------------------
	// Splitting / joining
	// -------------------------------------------------------------------------

	/**
	 * Splits {@code s} on the given delimiter character and returns the parts
	 * as an array. Unlike {@link String#split(String)} this method never
	 * discards trailing empty strings (equivalent to passing {@code -1} as the
	 * limit to {@link String#split(String, int)}).
	 *
	 * <p>Example: {@code split("a b c", ' ')} returns {@code ["a", "b", "c"]}.
	 *
	 * @param s         the string to split; must not be {@code null}
	 * @param separator the delimiter character
	 * @return array of parts; never {@code null}, never empty
	 */
	public static String[] split(String s, char separator) {
		return s.split(String.valueOf(separator), -1);
	}

	/**
	 * Joins the elements of {@code parts} with a single space character between
	 * each element.
	 *
	 * <p>Example: {@code joinWithSpace(List.of("hello", "world"))} returns
	 * {@code "hello world"}.
	 *
	 * @param parts the list of strings to join; must not be {@code null}
	 * @return the joined string; an empty string if {@code parts} is empty
	 */
	public static String joinWithSpace(List<String> parts) {
		return String.join(" ", parts);
	}

	// -------------------------------------------------------------------------
	// Substring operations
	// -------------------------------------------------------------------------

	/**
	 * Returns the left-most {@code len} characters of {@code s}, or {@code s}
	 * itself if its length is less than or equal to {@code len}.
	 *
	 * @param s   the string; must not be {@code null}
	 * @param len the maximum number of characters to return; must be &gt;= 0
	 * @return the left-most {@code len} characters
	 */
	public static String left(String s, int len) {
		if (len >= s.length()) {
			return s;
		}
		return s.substring(0, len);
	}

	/**
	 * Removes the given {@code suffix} from the end of {@code s}, if present.
	 * If {@code s} does not end with {@code suffix}, it is returned unchanged.
	 *
	 * @param s      the string; must not be {@code null}
	 * @param suffix the suffix to remove; must not be {@code null}
	 * @return the string without the trailing suffix
	 */
	public static String removeEnd(String s, String suffix) {
		if (s.endsWith(suffix)) {
			return s.substring(0, s.length() - suffix.length());
		}
		return s;
	}

	/**
	 * Returns {@code true} if {@code s} ends with {@code suffix}.
	 * Equivalent to {@link String#endsWith} but provided for API symmetry with
	 * the other methods in this class.
	 *
	 * @param s      the string; must not be {@code null}
	 * @param suffix the suffix; must not be {@code null}
	 * @return {@code true} if {@code s} ends with {@code suffix}
	 */
	public static boolean endsWith(String s, String suffix) {
		return s.endsWith(suffix);
	}

	// -------------------------------------------------------------------------
	// Search / replace
	// -------------------------------------------------------------------------

	/**
	 * Replaces all occurrences of {@code searchString} with {@code replacement}
	 * in {@code s}.
	 *
	 * @param s            the source string; must not be {@code null}
	 * @param searchString the literal string to search for; must not be
	 *                     {@code null}
	 * @param replacement  the replacement string; must not be {@code null}
	 * @return the modified string, or {@code s} if no occurrences were found
	 */
	public static String replace(String s, String searchString, String replacement) {
		return s.replace(searchString, replacement);
	}

	/**
	 * Replaces all characters in {@code s} that appear in {@code searchChars}
	 * with the corresponding character at the same index in
	 * {@code replaceChars}.
	 *
	 * <p>Characters in {@code searchChars} that have no corresponding entry in
	 * {@code replaceChars} are deleted from the result.
	 *
	 * <p>Example: {@code replaceChars("hello.world, ", "., ", "---")} returns
	 * {@code "hello-world-"}.
	 *
	 * @param s            the string; must not be {@code null}
	 * @param searchChars  the characters to search for; must not be {@code null}
	 * @param replaceChars the replacement characters; may be shorter than
	 *                     {@code searchChars}
	 * @return the modified string
	 */
	public static String replaceChars(String s, String searchChars, String replaceChars) {
		StringBuilder sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int idx = searchChars.indexOf(c);
			if (idx < 0) {
				// character not in search set – keep as-is
				sb.append(c);
			} else if (idx < replaceChars.length()) {
				// character has a replacement
				sb.append(replaceChars.charAt(idx));
			}
			// else: character is in search set but beyond replaceChars length – delete it
		}
		return sb.toString();
	}

	// -------------------------------------------------------------------------
	// Unicode / accent stripping
	// -------------------------------------------------------------------------

	/**
	 * Converts accented and special characters to their ASCII equivalents,
	 * e.g. for use in email addresses, usernames or domain names.
	 *
	 * <p>Handles characters that survive Unicode NFD normalization:
	 * {@code ß} becomes {@code ss}, {@code ł/Ł} becomes {@code l/L}.
	 *
	 * @param s the string to process; must not be {@code null}
	 * @return the latinized string
	 */
	public static String latinize(String s) {
		if (s.isEmpty()) {
			return s;
		}

		// handle special characters that survive NFD normalization
		String result = s.replace("ł", "l")
			             .replace("Ł", "L")
			             .replace("ß", "ss");

		// decompose and filter remaining diacritics
		String normalized = Normalizer.normalize(result, Normalizer.Form.NFD);

		StringBuilder sb = new StringBuilder(normalized.length());
		for (int i = 0; i < normalized.length(); i++) {
			char c = normalized.charAt(i);
			// Character.getType(c) == Character.NON_SPACING_MARK corresponds to \p{Mn}
			if (Character.getType(c) != Character.NON_SPACING_MARK) {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	// -------------------------------------------------------------------------
	// Non-ASCII escaping
	// -------------------------------------------------------------------------

	/**
	 * Escapes all non-ASCII characters in {@code input} as Unicode escape
	 * sequences of the form {@code &#92;uXXXX}.
	 *
	 * <p>ASCII characters (codepoints 0x00–0x7F) are passed through unchanged.
	 * Returns {@code null} when {@code input} is {@code null}.
	 *
	 * @param input the string to escape; may be {@code null}
	 * @return the escaped string, or {@code null} if {@code input} is
	 *         {@code null}
	 */
	public static @Nullable String escapeNonAscii(@Nullable String input) {
		if (input == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c > 0x7f) {
				sb.append(String.format("\\u%04x", (int) c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
