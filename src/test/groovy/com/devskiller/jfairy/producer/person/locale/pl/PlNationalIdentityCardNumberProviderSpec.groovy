/*
 * Copyright (c) 2013 Codearte and authors
 */

package com.devskiller.jfairy.producer.person.locale.pl

import java.time.LocalDate

import spock.lang.Specification

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.DateProducer

import static PlNationalIdentityCardNumberProvider.ISSUING_BEGIN
import static PlNationalIdentityCardNumberProvider.LETTER_WEIGHT
import static PlNationalIdentityCardNumberProvider.MAX_DIGITS_PART_VALUE

/**
 * @author mariuszs
 * @since 30.10.13.
 */
class PlNationalIdentityCardNumberProviderSpec extends Specification {

    private BaseProducer baseProducer = Mock(BaseProducer)
    private DateProducer dateGenerator = Mock(DateProducer)

    /**
     * http://en.wikipedia.org/wiki/Polish_identity_card
     */
    void "should generate proper id number"() {
        int max = (2013 - ISSUING_BEGIN) * LETTER_WEIGHT
        setup:
            baseProducer.randomBetween(max, (int) max + LETTER_WEIGHT) >> ('A'..'Z').size() // ABA
            baseProducer.randomInt(MAX_DIGITS_PART_VALUE) >> 0
        when:
            PlNationalIdentityCardNumberProvider generator = new PlNationalIdentityCardNumberProvider(dateGenerator, baseProducer)
            String id = generator.get(LocalDate.parse("2013-12-12"))
        then:
            id == "ABA300000"
            generator.isValid(id)
    }
}
