package com.devskiller.jfairy.producer.payment;

public class IBAN {

	private final String accountNumber;
	private final String checkDigit;
	private final String bankCode;
	private final String bban;
	private final String country;
	private final String nationalCheckDigit;
	private final String ibanNumber;

	public IBAN(String accountNumber, String checkDigit, String bankCode, String bban,
				String country, String nationalCheckDigit, String ibanNumber) {
		this.accountNumber = accountNumber;
		this.checkDigit = checkDigit;
		this.bankCode = bankCode;
		this.bban = bban;
		this.country = country;
		this.nationalCheckDigit = nationalCheckDigit;
		this.ibanNumber = ibanNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getCheckDigit() {
		return checkDigit;
	}

	public String getBankCode() {
		return bankCode;
	}

	public String getBban() {
		return bban;
	}

	public String getCountry() {
		return country;
	}

	public String getNationalCheckDigit() {
		return nationalCheckDigit;
	}

	public String getIbanNumber() {
		return ibanNumber;
	}
}
