package com.devskiller.jfairy.producer.util;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link StringUtils}.
 */
@DisplayName("StringUtils")
class StringUtilsTest {

	@Nested
	@DisplayName("isNotBlank")
	class IsNotBlank {

		@ParameterizedTest(name = "''{0}'' → false")
		@NullAndEmptySource
		@ValueSource(strings = {" ", "   ", "\t", "\n"})
		void returnsFalse(String s) {
			assertFalse(StringUtils.isNotBlank(s));
		}

		@ParameterizedTest(name = "''{0}'' → true")
		@ValueSource(strings = {"Have you tried turning it off and on again?", " Moss ", "Roy"})
		void returnsTrue(String s) {
			assertTrue(StringUtils.isNotBlank(s));
		}
	}

	@Nested
	@DisplayName("isNotEmpty")
	class IsNotEmpty {

		@ParameterizedTest(name = "''{0}'' → false")
		@NullAndEmptySource
		void returnsFalse(String s) {
			assertFalse(StringUtils.isNotEmpty(s));
		}

		@ParameterizedTest(name = "''{0}'' → true")
		@ValueSource(strings = {" ", "I am the IT department"})
		void returnsTrue(String s) {
			assertTrue(StringUtils.isNotEmpty(s));
		}
	}

	@Nested
	@DisplayName("lowerCase")
	class LowerCase {

		@ParameterizedTest(name = "''{0}'' → ''{1}''")
		@CsvSource(delimiter = '|', value = {
			"MOSS          | moss",
			"Roy Trenneman | roy trenneman",
			"IT CROWD      | it crowd",
			"Jen Barber    | jen barber",
		})
		void convertsCorrectly(String input, String expected) {
			assertEquals(expected, StringUtils.lowerCase(input));
		}
	}

	@Nested
	@DisplayName("capitalize / uncapitalize")
	class CapitalizeUncapitalize {

		@ParameterizedTest(name = "capitalize ''{0}'' → ''{1}''")
		@CsvSource(delimiter = '|', value = {
			"moss     | Moss",
			"REYNHOLM | REYNHOLM",
			"i        | I",
		})
		void capitalizeWorks(String input, String expected) {
			assertEquals(expected, StringUtils.capitalize(input));
		}

		@Test
		void capitalize_emptyString_returnsEmpty() {
			assertEquals("", StringUtils.capitalize(""));
		}

		@ParameterizedTest(name = "uncapitalize ''{0}'' → ''{1}''")
		@CsvSource(delimiter = '|', value = {
			"Moss     | moss",
			"Roy      | roy",
			"INTERNET | iNTERNET",
		})
		void uncapitalizeWorks(String input, String expected) {
			assertEquals(expected, StringUtils.uncapitalize(input));
		}

		@Test
		void uncapitalize_emptyString_returnsEmpty() {
			assertEquals("", StringUtils.uncapitalize(""));
		}
	}

	@Nested
	@DisplayName("leftPad")
	class LeftPad {

		@ParameterizedTest(name = "leftPad(''{0}'', {1}, ''0'') → ''{2}''")
		@CsvSource(delimiter = '|', value = {
			"0118999 | 10 | 0000118999",
			"999     |  3 | 999",
			"881999  |  4 | 881999",
		})
		void charVariant(String input, int size, String expected) {
			assertEquals(expected, StringUtils.leftPad(input, size, '0'));
		}

		@ParameterizedTest(name = "leftPad(''{0}'', {1}, \"0\") → ''{2}''")
		@CsvSource(delimiter = '|', value = {
			"119725 | 10 | 0000119725",
			"3      |  3 | 003",
			"42     |  5 | 00042",
			"hola   |  3 | hola",
		})
		void stringVariant(String input, int size, String expected) {
			assertEquals(expected, StringUtils.leftPad(input, size, "0"));
		}
	}

	@Nested
	@DisplayName("strip")
	class Strip {

		@ParameterizedTest(name = "strip(''{0}'', ''{1}'') → ''{2}''")
		@CsvSource(delimiter = '|', value = {
			"...Moss... | . | Moss",
			"!!!Roy!!!  | ! | Roy",
			"Jen        | . | Jen",
		})
		void stripsCorrectly(String input, String stripChars, String expected) {
			assertEquals(expected, StringUtils.strip(input, stripChars));
		}
	}

	@Nested
	@DisplayName("deleteWhitespace")
	class DeleteWhitespace {

		@ParameterizedTest(name = "''{0}'' → ''{1}''")
		@CsvSource(delimiter = '|', value = {
			"Moss Mega Usher | MossMegaUsher",
			"'Reynholm '     | Reynholm",
		})
		void removesWhitespace(String input, String expected) {
			assertEquals(expected, StringUtils.deleteWhitespace(input));
		}

		@Test
		void removesTabsAndNewlines() {
			assertEquals("0118999881999119725or3", StringUtils.deleteWhitespace("0118 999\t881 999\n119725 or 3"));
		}
	}

	@Nested
	@DisplayName("left")
	class Left {

		@ParameterizedTest(name = "left(''{0}'', {1}) → ''{2}''")
		@CsvSource(delimiter = '|', value = {
			"Reynholm Industries |  8 | Reynholm",
			"Moss                | 20 | Moss",
			"Roy                 |  3 | Roy",
		})
		void returnsCorrectSubstring(String input, int len, String expected) {
			assertEquals(expected, StringUtils.left(input, len));
		}

		@Test
		void zeroLength_returnsEmpty() {
			assertEquals("", StringUtils.left("Moss", 0));
		}
	}

	@Nested
	@DisplayName("removeEnd / endsWith")
	class RemoveEndEndsWith {

		@ParameterizedTest(name = "removeEnd(''{0}'', ''{1}'') → ''{2}''")
		@CsvSource(delimiter = '|', value = {
			"Moss! | ! | Moss",
			"Roy   | ! | Roy",
		})
		void removeEndWorks(String input, String suffix, String expected) {
			assertEquals(expected, StringUtils.removeEnd(input, suffix));
		}

		@ParameterizedTest(name = "endsWith(''{0}'', ''{1}'') → {2}")
		@CsvSource(delimiter = '|', value = {
			"Reynholm. | . | true",
			"Moss      | . | false",
		})
		void endsWithWorks(String input, String suffix, boolean expected) {
			assertEquals(expected, StringUtils.endsWith(input, suffix));
		}
	}

	@Nested
	@DisplayName("replace / replaceChars")
	class Replace {

		@ParameterizedTest(name = "replace(''{0}'', ''{1}'', ''{2}'') → ''{3}''")
		@CsvSource(delimiter = '|', value = {
			"Have you tried turning it off and on again? | off and on | on and off | Have you tried turning it on and off again?",
			"Moss | x | y | Moss",
			"Roy Roy Roy | Roy | Jen | Jen Jen Jen",
		})
		void replaceWorks(String input, String search, String replacement, String expected) {
			assertEquals(expected, StringUtils.replace(input, search, replacement));
		}

		@ParameterizedTest(name = "replaceChars(''{0}'', ''{1}'', ''{2}'') → ''{3}''")
		@CsvSource(delimiter = '|', value = {
			"Moss | eo  | --  | M-ss", // both chars have replacements
			"Moss | os  | x   | Mx",   // one replacement, two removals
			"Roy  | xyz | 123 | Ro2",  // one replacement
			"Richard Ayoade | Aa | _ | Richrd _yode", // one replacement, one removal
		})
		void replaceCharsWorks(String input, String searchChars, String replaceChars, String expected) {
			assertEquals(expected, StringUtils.replaceChars(input, searchChars, replaceChars));
		}
	}

	@Nested
	@DisplayName("latinize")
	class Latinize {

		@ParameterizedTest(name = "''{0}'' → ''{1}''")
		@CsvSource(delimiter = '|', value = {
			"Réynhölm           | Reynholm",
			"Möss               | Moss",
			"Straße             | Strasse",
			"Zażółć gęślą jaźń  | Zazolc gesla jazn",
			"łŁ                 | lL",
			"ßß                 | ssss",
			"Roy                | Roy",
		})
		void latinizesCorrectly(String input, String expected) {
			assertEquals(expected, StringUtils.latinize(input));
		}
	}

	@Nested
	@DisplayName("escapeNonAscii")
	class EscapeNonAscii {

		@Test
		void null_returnsNull() {
			assertNull(StringUtils.escapeNonAscii(null));
		}

		@ParameterizedTest(name = "''{0}'' → ''{1}''")
		@CsvSource(delimiter = '|', value = {
			"Moss | Moss",
			"Möss | M\\u00f6ss",
			"Réy  | R\\u00e9y",
		})
		void escapesCorrectly(String input, String expected) {
			assertEquals(expected, StringUtils.escapeNonAscii(input));
		}
	}

	@Nested
	@DisplayName("split")
	class Split {

		@Test
		void bySpace_returnsCorrectParts() {
			assertArrayEquals(new String[]{"Moss", "Roy", "Jen"},
				StringUtils.split("Moss Roy Jen", ' '));
		}

		@Test
		@DisplayName("trailing delimiter preserves trailing empty string")
		void trailingDelimiter_preservesTrailingEmpty() {
			assertArrayEquals(new String[]{"Moss", "Roy", ""},
				StringUtils.split("Moss Roy ", ' '));
		}

		@Test
		void noDelimiterPresent_returnsSingleElement() {
			assertArrayEquals(new String[]{"Reynholm"}, StringUtils.split("Reynholm", ' '));
		}
	}

	@Nested
	@DisplayName("joinWithSpace")
	class JoinWithSpace {

		@Test
		void multipleElements_joinedWithSpace() {
			assertEquals("Moss Roy Jen", StringUtils.joinWithSpace(List.of("Moss", "Roy", "Jen")));
		}

		@Test
		void singleElement_noSpaceAdded() {
			assertEquals("Moss", StringUtils.joinWithSpace(List.of("Moss")));
		}

		@Test
		void emptyList_returnsEmptyString() {
			assertEquals("", StringUtils.joinWithSpace(List.of()));
		}
	}

}
