package com.devskiller.jfairy.producer.person.locale.de;

import com.devskiller.jfairy.producer.person.locale.ContinentalAddress;

public class DeAddress extends ContinentalAddress {

    public DeAddress(String streetNumber, String street, String apartmentNumber, String city, String postalCode) {
        super(street, streetNumber, apartmentNumber, postalCode, city);
    }

    @Override
    protected String getApartmentMark() {
        return ", ";
    }
}
