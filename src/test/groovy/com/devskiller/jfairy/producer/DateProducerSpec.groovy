/*
 * Copyright (c) 2013 Codearte and authors
 */
package com.devskiller.jfairy.producer

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.Period

import spock.lang.Specification
import spock.lang.Unroll

class DateProducerSpec extends Specification {

    private static final int MAX_YEARS_IN_THE_PAST = 5

    private static final LocalDateTime CURRENT_DATE = LocalDateTime.parse("2013-11-09T01:16:00")
    private static final int CURRENT_YEAR = 2013
    private static final OffsetDateTime LATEST_DATE_IN_THE_PAST = OffsetDateTime.parse("2013-11-09T01:15:59+00:00")
    private static final OffsetDateTime SOME_DATE_IN_THE_PAST = OffsetDateTime.parse("2011-01-20T12:32:12+00:00")
    private static final OffsetDateTime ONE_YEAR_LATER = OffsetDateTime.parse("2014-11-09T01:16:00+00:00")

    private static final OffsetDateTime FIVE_YEARS_EARLIER_DATE = OffsetDateTime.parse("2008-11-09T01:16:00+00:00")
    private static final long LATEST_DATE_IN_THE_PAST_IN_MILLIS = LATEST_DATE_IN_THE_PAST.toInstant().toEpochMilli()
    private static final long SOME_DATE_IN_THE_PAST_IN_MILLIS = SOME_DATE_IN_THE_PAST.toInstant().toEpochMilli()
    private static final long FIVE_YEARS_EARLIER_DATE_IN_MILLIS = FIVE_YEARS_EARLIER_DATE.toInstant().toEpochMilli()

    private baseProducer = Spy(BaseProducer, constructorArgs: [new RandomGenerator()])
    private timeProviderMock = Mock(TimeProvider)
    private DateProducer sut = new DateProducer(baseProducer, timeProviderMock)

    def setup() {
        timeProviderMock.getCurrentTime() >> CURRENT_DATE
        timeProviderMock.getCurrentYear() >> CURRENT_YEAR
    }

    def "should generate date in the past"() {
        given:
            baseProducer.randomBetween(FIVE_YEARS_EARLIER_DATE_IN_MILLIS, LATEST_DATE_IN_THE_PAST_IN_MILLIS) >>
                SOME_DATE_IN_THE_PAST_IN_MILLIS
        when:
            LocalDateTime dateInThePast = sut.randomDateInThePast(MAX_YEARS_IN_THE_PAST)
        then:
            dateInThePast < CURRENT_DATE
            dateInThePast > FIVE_YEARS_EARLIER_DATE.toLocalDateTime()
            dateInThePast == SOME_DATE_IN_THE_PAST.toLocalDateTime()
    }

    def "should fail generate date in the past if passed value is negative"() {
        given:
            baseProducer.randomBetween(FIVE_YEARS_EARLIER_DATE_IN_MILLIS, LATEST_DATE_IN_THE_PAST_IN_MILLIS) >>
                SOME_DATE_IN_THE_PAST_IN_MILLIS
        when:
            sut.randomDateInThePast(-MAX_YEARS_IN_THE_PAST)
        then:
            thrown IllegalArgumentException
    }

    def "should be able to reach minimum date for date in the past"() {
        given:
            baseProducer.randomBetween(FIVE_YEARS_EARLIER_DATE_IN_MILLIS, LATEST_DATE_IN_THE_PAST_IN_MILLIS) >>
                FIVE_YEARS_EARLIER_DATE_IN_MILLIS
        when:
            LocalDateTime dateInThePast = sut.randomDateInThePast(MAX_YEARS_IN_THE_PAST)
        then:
            dateInThePast == FIVE_YEARS_EARLIER_DATE.toLocalDateTime()
    }

    def "maximum date should be before now with defined offset for date in the past"() {
        given:
            baseProducer.randomBetween(FIVE_YEARS_EARLIER_DATE_IN_MILLIS, LATEST_DATE_IN_THE_PAST_IN_MILLIS) >>
                LATEST_DATE_IN_THE_PAST_IN_MILLIS
        when:
            LocalDateTime dateInThePast = sut.randomDateInThePast(MAX_YEARS_IN_THE_PAST)
        then:
            dateInThePast == LATEST_DATE_IN_THE_PAST.toLocalDateTime()
    }

    @Unroll
    def "should generate date between years #fromYear - #toYear"() {
        given:
            baseProducer.randomBetween(_, _) >> {args -> (args[1] + args[0]) / 2}
        expect:
            sut.randomDateBetweenYears(fromYear, toYear) == expectedDate
        where:
            fromYear | toYear || expectedDate
            2009     | 2010   || LocalDateTime.parse("2009-12-31T23:59:30")
            2010     | 2010   || LocalDateTime.parse("2010-07-02T11:59:30")
            2015     | 2020   || LocalDateTime.parse("2017-12-31T23:59:30")
    }

    def "should generate date between specified year and now"() {
        given:
            baseProducer.randomBetween(FIVE_YEARS_EARLIER_DATE_IN_MILLIS, LATEST_DATE_IN_THE_PAST_IN_MILLIS) >>
                SOME_DATE_IN_THE_PAST_IN_MILLIS
        when:
            LocalDateTime dateInThePast = sut.randomDateBetweenYearAndNow(2008)
        then:
            dateInThePast < CURRENT_DATE
            dateInThePast > FIVE_YEARS_EARLIER_DATE.toLocalDateTime()
            dateInThePast == SOME_DATE_IN_THE_PAST.toLocalDateTime()
    }

    def "should generate date between now and specified period"() {
        when:
            LocalDateTime dateInFuturePeriod = sut.randomDateBetweenNowAndFuturePeriod(Period.ofMonths(12))
        then:
            dateInFuturePeriod >= CURRENT_DATE
            dateInFuturePeriod <= ONE_YEAR_LATER.toLocalDateTime()
    }

    def "should generate date between now and future offset"() {
        when:
            LocalDateTime dateInFuturePeriod = sut.randomDateInTheFuture(1)
        then:
            dateInFuturePeriod >= CURRENT_DATE
            dateInFuturePeriod <= ONE_YEAR_LATER.toLocalDateTime()
    }

    def "should generate date in future"() {
        when:
            LocalDateTime dateInFuturePeriod = sut.randomDateInTheFuture()
        then:
            dateInFuturePeriod >= CURRENT_DATE
    }

}
