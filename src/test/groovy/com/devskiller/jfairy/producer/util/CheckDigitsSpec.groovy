package com.devskiller.jfairy.producer.util

import spock.lang.Specification
import spock.lang.Unroll

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

class CheckDigitsSpec extends Specification {

    private BaseProducer baseProducer = new BaseProducer(new RandomGenerator())

    @Unroll
    def "should compute Luhn check digit #digit for #number"() {
        expect:
            CheckDigits.luhnDigit(number) == digit

        where:
            number            | digit
            "7992739871"      | 3
            "1234567890"      | 3
            "0000000000"      | 0
            "411111111111111" | 1
    }

    def "should reject non-digits in Luhn number"() {
        when:
            CheckDigits.luhnDigit("12a4")
        then:
            thrown(IllegalArgumentException)
    }

    def "should replace last letter with a different letter"() {
        when:
            def results = (1..200).collect { CheckDigits.replaceLastLetter("ABCZ", baseProducer) }
        then:
            results.every { it ==~ /ABC[A-Y]/ }
    }

    def "should reject non-letter as last character"() {
        when:
            CheckDigits.replaceLastLetter("ABC1", baseProducer)
        then:
            thrown(IllegalArgumentException)
    }
}
