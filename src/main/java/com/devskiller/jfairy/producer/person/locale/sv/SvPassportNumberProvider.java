package com.devskiller.jfairy.producer.person.locale.sv;

import com.devskiller.jfairy.producer.person.PassportNumberProvider;

import static com.devskiller.jfairy.producer.util.RandomUtils.randomNumeric;

/**
 * Swedish Passport Number (random number implementation)
 */
public class SvPassportNumberProvider implements PassportNumberProvider {

	@Override
	public String get() {
		return randomNumeric(8);
	}
}
