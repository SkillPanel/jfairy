
package com.devskiller.jfairy.producer.person.locale.es;

import com.devskiller.jfairy.producer.person.locale.ContinentalAddress;

public class EsAddress extends ContinentalAddress {

    public EsAddress(String street, String streetNumber, String apartmentNumber, String postalCode, String city) {
        super(street, streetNumber, apartmentNumber, postalCode, city);
    }

    @Override
    protected String getStreetNumberSeparator() {
        return ", ";
    }

    @Override
    public String getApartmentMark() {
        return " ";
    }
}

