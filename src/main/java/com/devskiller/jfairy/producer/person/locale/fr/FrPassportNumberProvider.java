package com.devskiller.jfairy.producer.person.locale.fr;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

/**
 * Provider for French Passport numbers.
 * <p>
 * Generates a 9-character alphanumeric string following the French biometric
 * passport format: 2 digits, 2 uppercase letters, and 5 digits (e.g., 12AB12345).
 *
 * @author Markus Spann
 * @since 0.8.3
 */
public class FrPassportNumberProvider implements PassportNumberProvider {

	private final BaseProducer baseProducer;

	public FrPassportNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return baseProducer.randomNumeric(2)
			 + baseProducer.randomAlphabetic(2)
			 + baseProducer.randomNumeric(5);
	}

}
