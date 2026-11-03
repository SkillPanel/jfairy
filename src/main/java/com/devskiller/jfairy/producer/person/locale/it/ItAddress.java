package com.devskiller.jfairy.producer.person.locale.it;

import com.devskiller.jfairy.producer.person.locale.ContinentalAddress;

/**
 * Italian address, e.g. "Via Roma, 12, int. 5" followed by "00184 Roma".
 *
 * @since 0.10.0
 */
public class ItAddress extends ContinentalAddress {

    public ItAddress(String street, String streetNumber, String apartmentNumber, String postalCode, String city) {
        super(street, streetNumber, apartmentNumber, postalCode, city);
    }

    /**
     * Separates the street from the house number with a comma, as in "Via Roma, 12".
     *
     * @return the separator between street and house number
     */
    @Override
    protected String getStreetNumberSeparator() {
        return ", ";
    }

    /**
     * Marks the apartment as "interno", as in "Via Roma, 12, int. 5".
     *
     * @return the text between house number and apartment number
     */
    @Override
    protected String getApartmentMark() {
        return ", int. ";
    }
}
