package com.devskiller.jfairy.producer.person.locale.ka;


import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.AbstractAddressProvider;

public class KaAddressProvider extends AbstractAddressProvider {


	public KaAddressProvider(DataMaster dataMaster, BaseProducer baseProducer) {
		super(dataMaster, baseProducer);
	}

	@Override
	public KaAddress get() {
		return new KaAddress(getStreet(), getStreetNumber(), getApartmentNumber(), getPostalCode(), getCity());
	}
}
