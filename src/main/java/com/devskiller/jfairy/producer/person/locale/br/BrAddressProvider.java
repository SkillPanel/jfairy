package com.devskiller.jfairy.producer.person.locale.br;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.AbstractAddressProvider;

public class BrAddressProvider extends AbstractAddressProvider {

	public BrAddressProvider(DataMaster dataMaster, BaseProducer baseProducer) {
		super(dataMaster, baseProducer);
	}

	@Override
	public BrAddress get() {
		return new BrAddress(getStreetNumber(), getStreet(), getApartmentNumber(),
				getPostalCode(), getCity());
	}
}
