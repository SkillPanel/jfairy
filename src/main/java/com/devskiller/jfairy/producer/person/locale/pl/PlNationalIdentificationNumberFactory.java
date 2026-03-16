package com.devskiller.jfairy.producer.person.locale.pl;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties;

public class PlNationalIdentificationNumberFactory implements NationalIdentificationNumberFactory {

	private final BaseProducer baseProducer;
	private final DateProducer dateProducer;

	public PlNationalIdentificationNumberFactory(BaseProducer baseProducer, DateProducer dateProducer) {
		this.baseProducer = baseProducer;
		this.dateProducer = dateProducer;
	}

	@Override
	public PlNationalIdentificationNumberProvider produceNationalIdentificationNumberProvider(NationalIdentificationNumberProperties.Property... properties) {
		return new PlNationalIdentificationNumberProvider(dateProducer, baseProducer, properties);
	}

}
