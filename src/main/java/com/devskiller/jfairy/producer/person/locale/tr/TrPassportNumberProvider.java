package com.devskiller.jfairy.producer.person.locale.tr;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

public class TrPassportNumberProvider implements PassportNumberProvider {

	private final BaseProducer baseProducer;

	public TrPassportNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return "U" + String.format("%08d", baseProducer.randomBetween(1, 99999999));
	}
}
