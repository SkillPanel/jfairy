package com.devskiller.jfairy.producer.person.locale.zh;


import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.AbstractAddressProvider;
import com.devskiller.jfairy.producer.person.Address;

/**
 * com.devskiller.jfairy.producer.person.locale.zh.ZhAddressProvider
 *
 * @author lhfcws
 * @since 2017/3/2
 */
public class ZhAddressProvider extends AbstractAddressProvider {

	public ZhAddressProvider(DataMaster dataMaster, BaseProducer baseProducer) {
		super(dataMaster, baseProducer);
	}

	@Override
	public Address get() {
		return new ZhAddress(getStreetNumber(), getStreet(), getApartmentNumber(),
			getCity(), getPostalCode());
	}

	public String getApartmentNumber() {
		if (baseProducer.trueOrFalse()) {
			String floor = String.valueOf(baseProducer.randomInt(38));
			String room = "0" + String.valueOf(baseProducer.randomInt(8));
			return floor + room;
		} else
			return "";
	}
}
