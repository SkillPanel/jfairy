package com.devskiller.jfairy.producer.person.locale.sk;

import com.devskiller.jfairy.producer.person.locale.ContinentalAddress;

public class SkAddress extends ContinentalAddress {

	public SkAddress(String street, String streetNumber, String apartmentNumber, String postalCode, String city) {
		super(street, streetNumber, apartmentNumber, postalCode, city);
	}

	@Override
	protected String getApartmentMark() {
		return ", ";
	}
}
