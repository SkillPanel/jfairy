package com.devskiller.jfairy.producer.person.locale.sv;


import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.AbstractAddressProvider;

public class SvAddressProvider extends AbstractAddressProvider {


	public SvAddressProvider(DataMaster dataMaster, BaseProducer baseProducer) {
		super(dataMaster, baseProducer);
	}

	@Override
	public String getApartmentNumber() {
		return baseProducer.randomInt(20) < 0 ? String.valueOf(baseProducer.randomInt(350)) : "";
	}

	@Override
	public SvAddress get() {
		return new SvAddress(getStreet(), getStreetNumber(), getApartmentNumber(),
				getPostalCode(), getCity());
	}

}
