package com.devskiller.jfairy.producer.person.locale.ja;

import static com.devskiller.jfairy.producer.util.StringUtils.isNotBlank;

import com.devskiller.jfairy.producer.person.locale.AbstractAddress;

/**
 * Japanese address, formatted in the conventional order:
 * postal code, prefecture/city, ward, block number and, optionally, room number.
 */
public class JaAddress extends AbstractAddress {

    private static final String ROOM = "号室";

    public JaAddress(String street, String streetNumber, String apartmentNumber, String postalCode, String city) {
        super(street, streetNumber, apartmentNumber, postalCode, city);
    }

    @Override
    public String getAddressLine1() {
        return "〒" + postalCode;
    }

    @Override
    public String getAddressLine2() {
        String line = city + street + streetNumber;
        return isNotBlank(apartmentNumber) ? line + " " + apartmentNumber + ROOM : line;
    }
}
