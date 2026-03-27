package com.devskiller.jfairy.producer.payment;

import java.util.function.Supplier;

public interface IBANProvider extends Supplier<IBAN> {

	@Override
	IBAN get();

	void fillCountryCode();

	void setCountry(String country);

}
