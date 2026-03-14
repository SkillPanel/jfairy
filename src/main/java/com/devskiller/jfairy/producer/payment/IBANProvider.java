package com.devskiller.jfairy.producer.payment;
import java.util.function.Supplier;


public interface IBANProvider extends Supplier<IBAN> {

	IBAN get();

	void fillCountryCode();

	void setNationalCheckDigit(String nationalCheckDigit);

	void setBranchCode(String branchCode);

	void setCountry(String country);

	void setAccountNumber(String accountNumber);

	void setBankCode(String bankCode);
}
