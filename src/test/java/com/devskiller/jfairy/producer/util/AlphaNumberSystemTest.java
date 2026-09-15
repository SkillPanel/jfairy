package com.devskiller.jfairy.producer.util;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlphaNumberSystemTest {

    @DisplayName("Should generate a complete alphabet list from A to Z")
    @Test
    void shouldGenerateCompleteAlphabetList() {
        List<Character> alphabet = AlphaNumberSystem.generateAlphabetList();

        assertEquals(26, alphabet.size(), "Alphabet should have 26 characters");
        assertEquals(Character.valueOf('A'), alphabet.get(0));
        assertEquals(Character.valueOf('Z'), alphabet.get(25));
    }

    @DisplayName("Should return an immutable list")
    @Test
    void shouldReturnImmutableList() {
        List<Character> alphabet = AlphaNumberSystem.generateAlphabetList();

        assertThrows(UnsupportedOperationException.class, () -> alphabet.add('!'),
            "The list should be unmodifiable");
    }

    @DisplayName("Should convert numbers to string correctly based on radix")
    @ParameterizedTest(name = "Convert {0} with base {1} should result in {2}")
    @CsvSource({
        "0,  26, A",
        "1,  26, B",
        "25, 26, Z",
        "26, 26, BA",
        "0,  10, A",
        "10, 10, BA"
    })
    void shouldConvertToStringCorrectly(int number, int base, String expected) {
        String result = AlphaNumberSystem.convertToString(number, base);

        assertEquals(expected, result);
    }

    @DisplayName("Should throw exception for invalid base")
    @Test
    void shouldThrowExceptionForInvalidBase() {
        assertThrows(IllegalArgumentException.class, () -> AlphaNumberSystem.convertToString(10, 0),
            "Base 0 should not be allowed");
        assertThrows(IllegalArgumentException.class, () -> AlphaNumberSystem.convertToString(10, 27),
            "Base > 26 should not be allowed");
    }

}
