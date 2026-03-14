package com.devskiller.jfairy.producer.company;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;

public class CompanyFactoryImpl implements CompanyFactory {

	private final BaseProducer baseProducer;
	private final DataMaster dataMaster;
	private final VATIdentificationNumberProvider vatIdentificationNumberProvider;

	public CompanyFactoryImpl(BaseProducer baseProducer,
	                   DataMaster dataMaster,
	                   VATIdentificationNumberProvider vatIdentificationNumberProvider) {
		this.baseProducer = baseProducer;
		this.dataMaster = dataMaster;
		this.vatIdentificationNumberProvider = vatIdentificationNumberProvider;
	}

	@Override
	public CompanyProvider produceCompany(CompanyProperties.CompanyProperty... companyProperties) {
		return new DefaultCompanyProvider(baseProducer, dataMaster, vatIdentificationNumberProvider, companyProperties);
	}
}
