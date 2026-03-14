package com.devskiller.jfairy.producer.person.locale.fr;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.AbstractAddressProvider;

public class FrAddressProvider extends AbstractAddressProvider {

	public FrAddressProvider(DataMaster dataMaster, BaseProducer baseProducer) {
		super(dataMaster, baseProducer);
	}

	@Override
	public FrAddress get() {
		return new FrAddress(getStreetNumber(), getStreet(), getApartmentNumber(),
				getPostalCode(), getCity());
	}
}
