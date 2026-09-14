package com.devskiller.jfairy.producer.person.locale.ja;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

/**
 * Japanese passport number: two uppercase letters followed by seven digits, e.g. {@code TZ1234567}.
 */
public class JaPassportNumberProvider implements PassportNumberProvider {

	private static final int LETTER_COUNT = 2;
	private static final int DIGIT_COUNT = 7;

	private final BaseProducer baseProducer;

	public JaPassportNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		StringBuilder sb = new StringBuilder(LETTER_COUNT + DIGIT_COUNT);
		for (int i = 0; i < LETTER_COUNT; i++) {
			sb.append((char) ('A' + baseProducer.randomInt(25)));
		}
		for (int i = 0; i < DIGIT_COUNT; i++) {
			sb.append(baseProducer.randomInt(9));
		}
		return sb.toString();
	}
}
