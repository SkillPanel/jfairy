package com.devskiller.jfairy.producer.person.locale.tr;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.AbstractAddressProvider;

public class TrAddressProvider extends AbstractAddressProvider {

	public TrAddressProvider(DataMaster dataMaster, BaseProducer baseProducer) {
		super(dataMaster, baseProducer);
	}

	@Override
	public TrAddress get() {
		return new TrAddress(getStreetNumber(), getStreet(), getApartmentNumber(),
				getPostalCode(), getCity());
	}
}
