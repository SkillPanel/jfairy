package com.devskiller.jfairy.producer.person.locale.zh;

import com.devskiller.jfairy.producer.person.locale.AbstractAddress;

/**
 * com.devskiller.jfairy.producer.person.locale.zh.ZhAddress
 *
 * @author lhfcws
 * @since 2017/3/2
 */
public class ZhAddress extends AbstractAddress {

    private static final String CITY_SUFFIX = "市";
    private static final String NUMBER_SUFFIX = "号";
    private static final String ROOM_SUFFIX = "房";
    private static final String POSTCODE_LABEL = "邮编";

    public ZhAddress(String streetNumber, String street, String apartmentNumber, String city, String postalCode) {
        super(street, streetNumber, apartmentNumber, postalCode, city);
    }

    @Override
    public String getAddressLine1() {
        String line = city + CITY_SUFFIX + street + streetNumber + NUMBER_SUFFIX;
        if (!apartmentNumber.isEmpty()) {
            return line + " " + apartmentNumber + ROOM_SUFFIX;
        } else {
            return line;
        }
    }

    @Override
    public String getAddressLine2() {
        return POSTCODE_LABEL + " " + postalCode;
    }
}
