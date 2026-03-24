package com.devskiller.jfairy.producer.person.locale.fr;

import java.time.Year;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;

/**
 * Provider for French National Identity Card numbers (Carte Nationale
 * d’Identité - CNI).
 *
 * <p>
 * Generates a 12-character alphanumeric string following the biometric format
 * introduced in 2021:
 * <ul>
 *   <li>Department code (2 digits, 01-95)</li>
 *   <li>Year of issuance (last 2 digits)</li>
 *   <li>Month of issuance (01-12)</li>
 *   <li>Random serial (6 alphanumeric characters)</li>
 * </ul>
 *
 * @author Markus Spann
 * @since 0.8.3
 */
public class FrNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

	private final BaseProducer baseProducer;

	public FrNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		// department (01-95)
		int dept = baseProducer.randomBetween(1, 95);

		// year (last two digits, from 2021 to current year)
		int currentYear = Year.now().getValue() % 100;
		int year = baseProducer.randomBetween(21, currentYear);

		// month (01-12)
		int month = baseProducer.randomBetween(1, 12);

		// random suffix (6 alphanumeric chars)
		String suffix = baseProducer.randomAlphanumeric(6);

		return String.format("%02d%02d%02d%s", dept, year, month, suffix);
	}

}
