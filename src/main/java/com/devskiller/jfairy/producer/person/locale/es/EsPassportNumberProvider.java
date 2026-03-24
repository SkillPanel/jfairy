package com.devskiller.jfairy.producer.person.locale.es;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

/**
 * @author graux
 * @since 26/04/2015
 */
public class EsPassportNumberProvider implements PassportNumberProvider {

	private final BaseProducer baseProducer;

	public EsPassportNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return baseProducer.randomAlphanumeric(9);
	}
}
