package com.devskiller.jfairy.producer.person.locale.ka;


import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

public class KaPassportNumberProvider implements PassportNumberProvider {

	private final BaseProducer baseProducer;


	public KaPassportNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	// District codes are not reachable at the moment.
	private String getDistrictCode() {
		return baseProducer.numerify("##");
	}

	// A system of Ministry of Inferior Office codes is not known at the moment.
	private String getStateOfficeCode(String district) {
		return baseProducer.numerify("###");
	}

	private String getNationalPersonalIdentificationNumber() {
		return baseProducer.numerify("######");
	}

	@Override
	public String get() {
		String districtCode = getDistrictCode();
		return districtCode + getStateOfficeCode(districtCode) + getNationalPersonalIdentificationNumber();
	}
}
