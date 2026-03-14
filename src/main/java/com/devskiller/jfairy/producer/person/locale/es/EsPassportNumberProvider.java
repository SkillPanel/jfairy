package com.devskiller.jfairy.producer.person.locale.es;

import com.devskiller.jfairy.producer.person.PassportNumberProvider;

import static com.devskiller.jfairy.producer.util.RandomUtils.randomAlphanumeric;

/**
 * @author graux
 * @since 26/04/2015
 */
public class EsPassportNumberProvider implements PassportNumberProvider {

	@Override
	public String get() {
		return randomAlphanumeric(9);
	}
}
