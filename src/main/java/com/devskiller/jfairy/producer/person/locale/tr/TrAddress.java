package com.devskiller.jfairy.producer.person.locale.tr;

import com.devskiller.jfairy.producer.person.locale.ContinentalAddress;

public class TrAddress extends ContinentalAddress {

	public TrAddress(String streetNumber, String street, String apartmentNumber, String postalCode, String city) {
		super(street, streetNumber, apartmentNumber, postalCode, city);
	}

	@Override
	protected String getStreetNumberSeparator() {
		return " No: ";
	}

	@Override
	protected String getApartmentMark() {
		return " Daire: ";
	}
}
