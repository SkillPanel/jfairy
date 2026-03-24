package com.devskiller.jfairy.producer.person.locale.sv;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;

/**
 * Swedish Identity Card Number (random number implementation)
 */
public class SvNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

	private final BaseProducer baseProducer;

	public SvNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return baseProducer.randomNumeric(8);
	}

}
