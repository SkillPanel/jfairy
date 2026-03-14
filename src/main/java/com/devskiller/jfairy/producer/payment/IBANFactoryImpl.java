package com.devskiller.jfairy.producer.payment;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;

public class IBANFactoryImpl implements IBANFactory {

	private final BaseProducer baseProducer;
	private final DataMaster dataMaster;

	public IBANFactoryImpl(BaseProducer baseProducer, DataMaster dataMaster) {
		this.baseProducer = baseProducer;
		this.dataMaster = dataMaster;
	}

	@Override
	public IBANProvider produceIBANProvider(IBANProperties.Property... properties) {
		return new DefaultIBANProvider(baseProducer, dataMaster, properties);
	}
}
