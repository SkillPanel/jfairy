package com.devskiller.jfairy.producer.payment;

import java.util.List;
import java.util.Optional;

import de.speedbanking.iban.Iban;
import de.speedbanking.iban.IbanRegistry;
import de.speedbanking.iban.RandomIban;
import org.jspecify.annotations.Nullable;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.Country;

/**
 * ALPHA: Under development
 */
public class DefaultIBANProvider implements IBANProvider {

	protected final DataMaster dataMaster;
	protected final BaseProducer baseProducer;
	protected String countryCode;

	public DefaultIBANProvider(BaseProducer baseProducer,
		                       DataMaster dataMaster,
		                       IBANProperties.Property... properties) {
		this.dataMaster = dataMaster;
		this.baseProducer = baseProducer;
		for (IBANProperties.Property property : properties) {
			property.apply(this);
		}
	}

	@Override
	public @Nullable IBAN get() {
		fillCountryCode();

		IbanRegistry reg = IbanRegistry.getByCode(countryCode);
		if (reg == null) {
			return null;
		}
		Iban iban = RandomIban.of(countryCode);

		return new IBAN(iban.getAccountNumber(),
						iban.getCheckDigits(),
						iban.getBankCode(),
						iban.getBban(),
						iban.getCountryCode(),
						iban.getNationalCheckDigit(),
						iban.toString());
	}

	@Override
	public void fillCountryCode() {
		if (countryCode == null) {
			List<Country> countries = Country.findCountryForLanguage(dataMaster.getLanguage());
			Country country = baseProducer.randomElement(countries);

			IbanRegistry r = IbanRegistry.getByCode(country.getCode());
			countryCode = Optional.ofNullable(r).map(IbanRegistry::name).orElse(null);
		}
	}

	@Override
	public void setCountry(String country) {
		this.countryCode = country;
	}

}
