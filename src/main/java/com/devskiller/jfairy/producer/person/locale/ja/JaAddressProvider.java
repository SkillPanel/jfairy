package com.devskiller.jfairy.producer.person.locale.ja;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.AbstractAddressProvider;
import com.devskiller.jfairy.producer.person.Address;

/**
 * Japanese address provider: generates a chōme-banchi-gō style block number
 * (e.g. {@code 3-11-17}) and, optionally, a room number.
 */
public class JaAddressProvider extends AbstractAddressProvider {

	private static final int CHOME_MAX = 9;
	private static final int BAN_MAX = 30;
	private static final int GO_MAX = 20;

	public JaAddressProvider(DataMaster dataMaster, BaseProducer baseProducer) {
		super(dataMaster, baseProducer);
	}

	@Override
	public Address get() {
		return new JaAddress(getStreet(), getStreetNumber(), getApartmentNumber(), getPostalCode(), getCity());
	}

	@Override
	public String getStreetNumber() {
		int chome = baseProducer.randomBetween(1, CHOME_MAX);
		int ban = baseProducer.randomBetween(1, BAN_MAX);
		int go = baseProducer.randomBetween(1, GO_MAX);
		return chome + "-" + ban + "-" + go;
	}

	@Override
	public String getApartmentNumber() {
		return baseProducer.trueOrFalse() ? String.valueOf(baseProducer.randomBetween(101, 999)) : "";
	}
}
