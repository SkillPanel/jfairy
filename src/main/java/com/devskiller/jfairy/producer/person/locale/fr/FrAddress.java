package com.devskiller.jfairy.producer.person.locale.fr;

import com.devskiller.jfairy.producer.person.locale.ContinentalAddress;

public class FrAddress extends ContinentalAddress {

	public FrAddress(String streetNumber, String street, String apartmentNumber, String postalCode, String city) {
		super(street, streetNumber, apartmentNumber, postalCode, city);
	}

	@Override
	protected String getApartmentMark() {
		return " Apt ";
	}
}
