package com.devskiller.jfairy.data

import spock.lang.Specification

import com.devskiller.jfairy.producer.BaseProducer

class WeightedListSpec extends Specification {

    def "splits on commas, trims and skips empty elements"() {
        when:
            WeightedList list = WeightedList.parse('A, B ,, ')

        then:
            list.values() == ['A', 'B']
            list.elements() == ['A', 'B']
            !list.isEmpty()
    }

    def "separates values from weights"() {
        when:
            WeightedList list = WeightedList.parse('Nowak*98387, Adamiec')

        then:
            list.values() == ['Nowak', 'Adamiec']
            list.elements() == ['Nowak*98387', 'Adamiec']
    }

    def "tolerates whitespace around the weight separator"() {
        expect:
            WeightedList.parse('Nowak * 5').values() == ['Nowak']
    }

    def "an empty value is an empty list"() {
        expect:
            WeightedList.parse(' , ').isEmpty()
    }

    def "picks an unweighted list uniformly, as before weights existed"() {
        given:
            BaseProducer baseProducer = Mock()

        when:
            String picked = WeightedList.parse('A,B').pick(baseProducer)

        then:
            1 * baseProducer.randomElement(['A', 'B']) >> 'B'
            0 * baseProducer.randomBetween(_, _)
            picked == 'B'
    }

    def "picks the element whose weight interval contains the drawn number #drawn"() {
        given:
            BaseProducer baseProducer = Stub {
                randomBetween(1L, 4L) >> drawn
            }

        expect:
            WeightedList.parse('A*1,B*3').pick(baseProducer) == expected

        where:
            drawn | expected
            1L    | 'A'
            2L    | 'B'
            4L    | 'B'
    }

    def "an element without a weight weighs 1 in a weighted list"() {
        given:
            BaseProducer baseProducer = Stub {
                randomBetween(1L, 3L) >> 3L
            }

        expect:
            WeightedList.parse('A*2,B').pick(baseProducer) == 'B'
    }

    def "rejects the malformed element #element"() {
        when:
            WeightedList.parse("Adamiec,${element}")

        then:
            IllegalArgumentException ex = thrown()
            ex.message == message

        where:
            element    | message
            'Nowak*0'  | "Element 'Nowak*0' must have a positive integer weight"
            'Nowak*-1' | "Element 'Nowak*-1' must have a positive integer weight"
            'Nowak*x'  | "Element 'Nowak*x' must have a positive integer weight"
            'Nowak*'   | "Element 'Nowak*' must have a positive integer weight"
            'A*1*2'    | "Element 'A*1*2' must have a positive integer weight"
            '*5'       | "Element '*5' has a weight but no value"
    }

    def "rejects weights that add up past Long.MAX_VALUE"() {
        when:
            WeightedList.parse("A*${Long.MAX_VALUE},B*1")

        then:
            IllegalArgumentException ex = thrown()
            ex.message == "Weights add up to more than ${Long.MAX_VALUE}"
    }
}
