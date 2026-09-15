
package com.devskiller.jfairy.producer.person.locale.pl;

import com.devskiller.jfairy.producer.person.locale.ContinentalAddress;

public class PlAddress extends ContinentalAddress {

    public PlAddress(String street, String streetNumber, String apartmentNumber, String postalCode, String city) {
        super(street, streetNumber, apartmentNumber, postalCode, city);
    }

    @Override
    protected String getApartmentMark() {
        return ", ";
    }
}
