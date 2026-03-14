package com.devskiller.jfairy.producer.person.locale.sv;


import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;

import static com.devskiller.jfairy.producer.util.RandomUtils.randomNumeric;

/**
 * Swedish Identity Card Number (random number implementation)
 */
public class SvNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

	private final DateProducer dateProducer;
	private final BaseProducer baseProducer;


	public SvNationalIdentityCardNumberProvider(DateProducer dateProducer, BaseProducer baseProducer) {
		this.dateProducer = dateProducer;
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return randomNumeric(8);
	}
}
