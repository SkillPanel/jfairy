package com.devskiller.jfairy.producer.person.locale.sv;


import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties;

public class SvNationalIdentificationNumberFactory implements NationalIdentificationNumberFactory {

	private final BaseProducer baseProducer;
	private final DateProducer dateProducer;


	public SvNationalIdentificationNumberFactory(BaseProducer baseProducer, DateProducer dateProducer) {
		this.baseProducer = baseProducer;
		this.dateProducer = dateProducer;
	}

	@Override
	public SvNationalIdentificationNumberProvider produceNationalIdentificationNumberProvider(NationalIdentificationNumberProperties.Property... properties) {
		return new SvNationalIdentificationNumberProvider(dateProducer, baseProducer, properties);
	}
}
