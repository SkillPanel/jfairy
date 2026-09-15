package com.devskiller.jfairy.producer.person.locale;

import com.devskiller.jfairy.producer.person.Address;

/**
 * A base of all addresses. It carries all needed fields, but leaves most of the formatting to subclasses.
 */
public abstract class AbstractAddress implements Address {
    protected final String street;
    protected final String streetNumber;
    protected final String apartmentNumber;
    protected final String postalCode;
    protected final String city;

    public AbstractAddress(String street, String streetNumber, String apartmentNumber, String postalCode, String city) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.apartmentNumber = apartmentNumber;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public String getStreetNumber() {
        return streetNumber;
    }

    @Override
    public String getApartmentNumber() {
        return apartmentNumber;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public abstract String getAddressLine1();

    @Override
    public abstract String getAddressLine2();

    @Override
    public String toString() {
        return getAddressLine1() + System.lineSeparator() + getAddressLine2();
    }
}
