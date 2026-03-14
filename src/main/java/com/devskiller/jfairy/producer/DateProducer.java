/*
 * Copyright (c) 2013. Codearte
 */

package com.devskiller.jfairy.producer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneOffset;

import com.devskiller.jfairy.producer.util.ValidateUtils;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public class DateProducer {

	static final int SECONDS_BEFORE_TO_BE_IN_THE_PAST = 1;

	private final BaseProducer baseProducer;
	private final TimeProvider timeProvider;


	public DateProducer(BaseProducer baseProducer, TimeProvider timeProvider) {
		this.baseProducer = baseProducer;
		this.timeProvider = timeProvider;
	}

	public LocalDateTime randomDateInThePast(int maxYearsEarlier) {
		ValidateUtils.isTrue(maxYearsEarlier >= 0, "%d has to be >= 0", maxYearsEarlier);
		LocalDateTime currentDate = timeProvider.getCurrentTime();
		LocalDateTime latestDateInThePast = currentDate.minusSeconds(SECONDS_BEFORE_TO_BE_IN_THE_PAST);
		LocalDateTime maxYearsEarlierDate = currentDate.minusYears(maxYearsEarlier);
		return randomDateBetweenTwoDates(maxYearsEarlierDate, latestDateInThePast);
	}

	public LocalDateTime randomDateBetweenYearAndNow(int fromYear) {
		int actualYear = timeProvider.getCurrentYear();
		return randomDateInThePast(actualYear - fromYear);
	}

	public LocalDate randomDateBetweenTwoDates(LocalDate from, LocalDate to) {
		long epochDay = baseProducer.randomBetween(from.toEpochDay(), to.toEpochDay());
		return LocalDate.ofEpochDay(epochDay);
	}

	public LocalDateTime randomDateBetweenTwoDates(LocalDateTime from, LocalDateTime to) {
		long between = baseProducer.randomBetween(from.toInstant(ZoneOffset.UTC).toEpochMilli(),
			to.toInstant(ZoneOffset.UTC).toEpochMilli());
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(between), ZoneOffset.UTC);
	}

	public LocalDateTime randomDateBetweenYears(int fromYear, int toYear) {
		ValidateUtils.isTrue(fromYear <= toYear, "%d has to be <= %d", fromYear, toYear);
		LocalDateTime fromDate = getDateForFirstDayForGivenYear(fromYear);
		LocalDateTime toDate = getDateForLastDayForGivenYear(toYear);
		return randomDateBetweenTwoDates(fromDate, toDate);
	}

	private LocalDateTime getDateForLastDayForGivenYear(int year) {
		return LocalDateTime.of(year, Month.JANUARY, 1, 23, 59).with(lastDayOfYear());
	}

	private LocalDateTime getDateForFirstDayForGivenYear(int year) {
		return LocalDateTime.of(year, Month.JANUARY, 1, 0, 0).with(firstDayOfYear());
	}

	public LocalDateTime randomDateBetweenNowAndFuturePeriod(Period futurePeriod) {
		LocalDateTime now = timeProvider.getCurrentTime();
		return randomDateBetweenTwoDates(now, now.plus(futurePeriod));
	}

	public LocalDateTime randomDateInTheFuture(int years) {
		return randomDateBetweenNowAndFuturePeriod(Period.ofYears(years));
	}

	public LocalDateTime randomDateInTheFuture() {
		return randomDateInTheFuture(100);
	}
}
