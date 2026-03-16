package com.devskiller.jfairy.producer.person.locale.en;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.AbstractAddressProvider;

public class EnAddressProvider extends AbstractAddressProvider {

	public EnAddressProvider(DataMaster dataMaster, BaseProducer baseProducer) {
		super(dataMaster, baseProducer);
	}

	@Override
	public EnAddress get() {
		return new EnAddress(getStreetNumber(), getStreet(), getApartmentNumber(),
				getCity(), getPostalCode());
	}

}
