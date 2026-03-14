package com.devskiller.jfairy.producer.person.locale.en;

import com.devskiller.jfairy.producer.person.PassportNumberProvider;

import static com.devskiller.jfairy.producer.util.RandomUtils.randomAlphanumeric;

/**
 * @author Olga Maciaszek-Sharma
 * @since 15.03.15
 */
public class EnPassportNumberProvider implements PassportNumberProvider {

	@Override
	public String get() {
		return randomAlphanumeric(9);
	}
}
