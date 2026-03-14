package com.devskiller.jfairy.producer.person.locale;

import static com.devskiller.jfairy.producer.util.StringUtils.isNotBlank;

/**
 * An address format typical for European countries but the UK and ex-Soviet union.
 */
public abstract class ContinentalAddress extends AbstractAddress {
	public ContinentalAddress(String street, String streetNumber, String apartmentNumber, String postalCode, String city) {
		super(street, streetNumber, apartmentNumber, postalCode, city);
	}

	protected abstract String getApartmentMark();

	protected String getStreetNumberSeparator() {
		return " ";
	}

	@Override
	public String getAddressLine1() {
		return street + getStreetNumberSeparator() + streetNumber
				+ (isNotBlank(apartmentNumber) ? getApartmentMark() + apartmentNumber : "");
	}

	@Override
	public String getAddressLine2() {
		return postalCode + " " + city;
	}
}
