package com.devskiller.jfairy.producer.person.locale.en;

import static com.devskiller.jfairy.producer.util.StringUtils.isNotBlank;

import com.devskiller.jfairy.producer.person.locale.AbstractAddress;

public class EnAddress extends AbstractAddress {

    public EnAddress(String streetNumber, String street, String apartmentNumber, String city, String postalCode) {
        super(street, streetNumber, apartmentNumber, postalCode, city);
    }

    @Override
    public String getAddressLine1() {
        return streetNumber + " " + street
                + (isNotBlank(apartmentNumber) ? " APT " + apartmentNumber : "");
    }

    @Override
    public String getAddressLine2() {
        return city + " " + postalCode;
    }
}
