package com.devskiller.jfairy.producer.person;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.company.CompanyFactory;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.TimeProvider;

public class PersonFactoryImpl implements PersonFactory {

	private final DataMaster dataMaster;
	private final DateProducer dateProducer;
	private final BaseProducer baseProducer;
	private final AddressProvider addressProvider;
	private final NationalIdentificationNumberFactory nationalIdentificationNumberFactory;
	private final NationalIdentityCardNumberProvider nationalIdentityCardNumberProvider;
	private final PassportNumberProvider passportNumberProvider;
	
	private final TimeProvider timeProvider;
	private final CompanyFactory companyFactory;

	public PersonFactoryImpl(DataMaster dataMaster,
	                  DateProducer dateProducer,
	                  BaseProducer baseProducer,
	                  AddressProvider addressProvider,
	                  NationalIdentificationNumberFactory nationalIdentificationNumberFactory,
	                  NationalIdentityCardNumberProvider nationalIdentityCardNumberProvider,
	                  PassportNumberProvider passportNumberProvider,
	                  TimeProvider timeProvider,
	                  CompanyFactory companyFactory) {
		this.dataMaster = dataMaster;
		this.dateProducer = dateProducer;
		this.baseProducer = baseProducer;
		this.addressProvider = addressProvider;
		this.nationalIdentificationNumberFactory = nationalIdentificationNumberFactory;
		this.nationalIdentityCardNumberProvider = nationalIdentityCardNumberProvider;
		this.passportNumberProvider = passportNumberProvider;
		this.timeProvider = timeProvider;
		this.companyFactory = companyFactory;
	}

	@Override
	public PersonProvider producePersonProvider(PersonProperties.PersonProperty... personProperties) {
		return new DefaultPersonProvider(dataMaster, dateProducer, baseProducer, addressProvider,
				nationalIdentificationNumberFactory, nationalIdentityCardNumberProvider,
				passportNumberProvider, timeProvider, companyFactory, personProperties);
	}
}
