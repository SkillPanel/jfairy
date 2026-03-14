package com.devskiller.jfairy;

import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.AddressProvider;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

/**
 * Container for locale-specific provider implementations
 */
class LocaleSpecificProviders {
	final NationalIdentificationNumberFactory nationalIdentificationNumberFactory;
	final NationalIdentityCardNumberProvider nationalIdentityCardNumberProvider;
	final VATIdentificationNumberProvider vatIdentificationNumberProvider;
	final AddressProvider addressProvider;
	final PassportNumberProvider passportNumberProvider;

	LocaleSpecificProviders(NationalIdentificationNumberFactory nationalIdentificationNumberFactory,
	                        NationalIdentityCardNumberProvider nationalIdentityCardNumberProvider,
	                        VATIdentificationNumberProvider vatIdentificationNumberProvider,
	                        AddressProvider addressProvider,
	                        PassportNumberProvider passportNumberProvider) {
		this.nationalIdentificationNumberFactory = nationalIdentificationNumberFactory;
		this.nationalIdentityCardNumberProvider = nationalIdentityCardNumberProvider;
		this.vatIdentificationNumberProvider = vatIdentificationNumberProvider;
		this.addressProvider = addressProvider;
		this.passportNumberProvider = passportNumberProvider;
	}
}
