package com.devskiller.jfairy.producer.person.locale.de;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

/**
 * German passport (known as Reisepass)
 * <p>
 * <a href="https://en.wikipedia.org/wiki/German_passport">German passport</a>
 *
 * @author Roland Weisleder
 */
public class DePassportNumberProvider implements PassportNumberProvider {

	private static final String[] PASSPORT_TYPE_LETTERS = {"C", "F", "G", "H", "J", "K"};

	private static final String VALID_NUMBER_PATTERN = "^[CFGHJK][0-9]{8}$";

	private final BaseProducer baseProducer;

	public DePassportNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return baseProducer.randomElement(PASSPORT_TYPE_LETTERS) + baseProducer.randomNumeric(8);
	}

	public boolean isValid(String passportNumber) {
		return passportNumber.matches(VALID_NUMBER_PATTERN);
	}

}
