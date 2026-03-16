package com.devskiller.jfairy;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.company.CompanyFactory;
import com.devskiller.jfairy.producer.net.IPNumberProducer;
import com.devskiller.jfairy.producer.net.NetworkProducer;
import com.devskiller.jfairy.producer.payment.CreditCardProvider;
import com.devskiller.jfairy.producer.payment.IBANFactory;
import com.devskiller.jfairy.producer.person.PersonFactory;
import com.devskiller.jfairy.producer.text.TextProducer;
import com.devskiller.jfairy.producer.text.TextProducerInternal;

class FairyFactoryImpl implements FairyFactory {

	private final TextProducerInternal textProducerInternal;
	private final BaseProducer baseProducer;
	private final PersonFactory personFactory;
	private final IPNumberProducer ipNumberProducer;
	private final DateProducer dateProducer;
	private final CreditCardProvider creditCardProvider;
	private final CompanyFactory companyFactory;
	private final IBANFactory ibanFactory;

	FairyFactoryImpl(TextProducerInternal textProducerInternal,
	                 BaseProducer baseProducer,
	                 PersonFactory personFactory,
	                 IPNumberProducer ipNumberProducer,
	                 DateProducer dateProducer,
	                 CreditCardProvider creditCardProvider,
	                 CompanyFactory companyFactory,
	                 IBANFactory ibanFactory) {
		this.textProducerInternal = textProducerInternal;
		this.baseProducer = baseProducer;
		this.personFactory = personFactory;
		this.ipNumberProducer = ipNumberProducer;
		this.dateProducer = dateProducer;
		this.creditCardProvider = creditCardProvider;
		this.companyFactory = companyFactory;
		this.ibanFactory = ibanFactory;
	}

	@Override
	public Fairy createFairy() {
		TextProducer textProducer = new TextProducer(textProducerInternal, baseProducer);
		NetworkProducer networkProducer = new NetworkProducer(ipNumberProducer);

		return new Fairy(textProducer, personFactory, networkProducer, baseProducer,
		                 dateProducer, creditCardProvider, companyFactory, ibanFactory);
	}
}
