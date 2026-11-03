package com.devskiller.jfairy.producer.person.locale.it;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.AbstractAddressProvider;

/**
 * Provides Italian addresses.
 *
 * @since 0.10.0
 */
public class ItAddressProvider extends AbstractAddressProvider {

    public ItAddressProvider(DataMaster dataMaster, BaseProducer baseProducer) {
        super(dataMaster, baseProducer);
    }

    /**
     * Generates a random Italian address.
     *
     * @return a new address
     */
    @Override
    public ItAddress get() {
        return new ItAddress(getStreet(), getStreetNumber(), getApartmentNumber(), getPostalCode(), getCity());
    }
}
