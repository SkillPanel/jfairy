package com.devskiller.jfairy.producer.person.locale.sv;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

/**
 * Swedish Passport Number (random number implementation)
 */
public class SvPassportNumberProvider implements PassportNumberProvider {

	private final BaseProducer baseProducer;

	public SvPassportNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return baseProducer.randomNumeric(8);
	}
}
