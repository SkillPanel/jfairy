package com.devskiller.jfairy;

import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.AddressProvider;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

/**
 * Container for locale-specific provider implementations
 */
record LocaleSpecificProviders(
		NationalIdentificationNumberFactory nationalIdentificationNumberFactory,
		NationalIdentityCardNumberProvider nationalIdentityCardNumberProvider,
		VATIdentificationNumberProvider vatIdentificationNumberProvider,
		AddressProvider addressProvider,
		PassportNumberProvider passportNumberProvider) {
}
