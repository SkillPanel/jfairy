package com.devskiller.jfairy.producer.company.locale.ja;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;

/**
 * Japanese qualified invoice issuer registration number (適格請求書発行事業者登録番号):
 * the letter {@code T} followed by the 13-digit corporate number.
 */
public class JaVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

	private static final int DIGIT_COUNT = 13;

	private final BaseProducer baseProducer;

	public JaVATIdentificationNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		StringBuilder sb = new StringBuilder(1 + DIGIT_COUNT);
		sb.append('T');
		for (int i = 0; i < DIGIT_COUNT; i++) {
			sb.append(baseProducer.randomInt(9));
		}
		return sb.toString();
	}
}
