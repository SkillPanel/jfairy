package com.devskiller.jfairy.producer.payment;

public class IBAN {

	private final String accountNumber;
	private final String identificationNumber;
	private final String branchCode;
	private final String checkDigit;
	private final String accountType;
	private final String bankCode;
	private final String bban;
	private final String country;
	private final String nationalCheckDigit;
	private final String ownerAccountType;
	private final String ibanNumber;

	public IBAN(String accountNumber, String identificationNumber, String branchCode, String checkDigit,
				String accountType, String bankCode, String bban, String country, String nationalCheckDigit, String ownerAccountType, String ibanNumber) {
		this.accountNumber = accountNumber;
		this.identificationNumber = identificationNumber;
		this.branchCode = branchCode;
		this.checkDigit = checkDigit;
		this.accountType = accountType;
		this.bankCode = bankCode;
		this.bban = bban;
		this.country = country;
		this.nationalCheckDigit = nationalCheckDigit;
		this.ownerAccountType = ownerAccountType;
		this.ibanNumber = ibanNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public String getCheckDigit() {
		return checkDigit;
	}

	public String getAccountType() {
		return accountType;
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

	public String getOwnerAccountType() {
		return ownerAccountType;
	}

	public String getIbanNumber() {
		return ibanNumber;
	}
}
