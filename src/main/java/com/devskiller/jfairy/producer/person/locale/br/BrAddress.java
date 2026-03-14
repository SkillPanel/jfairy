package com.devskiller.jfairy.producer.person.locale.br;

import com.devskiller.jfairy.producer.person.locale.ContinentalAddress;

public class BrAddress extends ContinentalAddress {

	public BrAddress(String streetNumber, String street, String apartmentNumber, String postalCode, String city) {
		super(street, streetNumber, apartmentNumber, postalCode, city);
	}

	@Override
	protected String getStreetNumberSeparator() {
		return ", ";
	}

	@Override
	protected String getApartmentMark() {
		return " APT ";
	}
}
