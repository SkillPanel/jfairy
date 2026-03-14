package com.devskiller.jfairy.producer.payment;

import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.iban4j.UnsupportedCountryException;
import org.jspecify.annotations.Nullable;

import java.util.IllegalFormatCodePointException;
import java.util.List;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.Country;

import static com.devskiller.jfairy.producer.util.StringUtils.isNotEmpty;

/**
 * ALPHA: Under development
 */
public class DefaultIBANProvider implements IBANProvider {

	protected DataMaster dataMaster;
	protected BaseProducer baseProducer;
	protected CountryCode countryCode;
	protected String accountNumber;
	protected String bankCode;
	protected String branchCode;
	protected String nationalCheckDigit;


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
		try {

			fillCountryCode();

			try {

				Iban.Builder builder = new Iban.Builder()
					.countryCode(countryCode)
					.bankCode(bankCode)
					.branchCode(branchCode)
					.nationalCheckDigit(nationalCheckDigit);
				if (isNotEmpty(accountNumber)) {
					builder.accountNumber(accountNumber);
				}
				Iban iban = builder.buildRandom();

				String identificationNumber = iban.getIdentificationNumber();
				String checkDigit = iban.getCheckDigit();
				String accountType = iban.getAccountType();
				String bban = iban.getBban();
				String ownerAccountType = iban.getOwnerAccountType();
				String ibanNumber = iban.toString();

				return new IBAN(accountNumber, identificationNumber, branchCode, checkDigit,
					accountType, bankCode, bban, countryCode.getName(), nationalCheckDigit,
					ownerAccountType, ibanNumber);
			} catch (UnsupportedCountryException e) {
				return null;
			}
		} catch (IllegalFormatCodePointException e) {
			throw new IllegalArgumentException("Invalid iban " + e.getMessage(), e);
		}
	}

	@Override
	public void fillCountryCode() {
		if (countryCode == null) {
			List<Country> countries = Country.findCountryForLanguage(dataMaster.getLanguage());
			Country country = baseProducer.randomElement(countries);
			countryCode = CountryCode.valueOf(country.getCode());
		}
	}

	@Override
	public void setNationalCheckDigit(String nationalCheckDigit) {
		this.nationalCheckDigit = nationalCheckDigit;
	}

	@Override
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	@Override
	public void setCountry(String country) {
		this.countryCode = CountryCode.valueOf(country);
	}

	@Override
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

}
