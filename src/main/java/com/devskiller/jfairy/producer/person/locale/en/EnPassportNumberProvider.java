package com.devskiller.jfairy.producer.person.locale.en;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

/**
 * @author Olga Maciaszek-Sharma
 * @since 15.03.15
 */
public class EnPassportNumberProvider implements PassportNumberProvider {

	private final BaseProducer baseProducer;

	public EnPassportNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return baseProducer.randomAlphanumeric(9);
	}
}
