package com.devskiller.jfairy.producer.person;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;

public abstract class AbstractAddressProvider implements AddressProvider {

    private static final String POSTAL_CODE_FORMAT = "postalCodes";

    private static final String CITY = "cities";

    private static final String STREET = "streets";

    protected final BaseProducer baseProducer;

    protected final DataMaster dataMaster;

    public AbstractAddressProvider(DataMaster dataMaster, BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
        this.dataMaster = dataMaster;
    }

    public String getCity() {
        return dataMaster.getRandomValue(CITY);
    }

    public String getPostalCode() {
        String postalCodeFormat = dataMaster.getRandomValue(POSTAL_CODE_FORMAT);
        return baseProducer.numerify(postalCodeFormat);
    }

    public String getStreet() {
        return dataMaster.getRandomValue(STREET);
    }

    public String getStreetNumber() {
        return String.valueOf(baseProducer.randomBetween(1, 199));
    }

    public String getApartmentNumber() {
        return baseProducer.trueOrFalse() ? String.valueOf(baseProducer.randomBetween(1, 350)) : "";
    }

}
