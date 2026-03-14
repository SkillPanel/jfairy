package com.devskiller.jfairy;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.locale.NoNationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.locale.de.DeAddressProvider;
import com.devskiller.jfairy.producer.person.locale.de.DeNationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.person.locale.de.DePassportNumberProvider;
import com.devskiller.jfairy.producer.company.locale.de.DeVATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.locale.en.EnAddressProvider;
import com.devskiller.jfairy.producer.person.locale.en.EnNationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.person.locale.en.EnPassportNumberProvider;
import com.devskiller.jfairy.producer.company.locale.en.EnVATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.locale.fr.FrAddressProvider;
import com.devskiller.jfairy.producer.person.locale.es.EsAddressProvider;
import com.devskiller.jfairy.producer.person.locale.es.EsNationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.person.locale.es.EsPassportNumberProvider;
import com.devskiller.jfairy.producer.company.locale.es.EsVATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.locale.ka.KaAddressProvider;
import com.devskiller.jfairy.producer.person.locale.ka.KaNationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.person.locale.ka.KaPassportNumberProvider;
import com.devskiller.jfairy.producer.company.locale.ka.KaVATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.locale.pl.PlAddressProvider;
import com.devskiller.jfairy.producer.person.locale.pl.PlNationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.locale.pl.PlNationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.person.locale.pl.PlPassportNumberProvider;
import com.devskiller.jfairy.producer.company.locale.pl.PlVATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.locale.sv.SvAddressProvider;
import com.devskiller.jfairy.producer.person.locale.sv.SvNationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.locale.sv.SvNationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.person.locale.sv.SvPassportNumberProvider;
import com.devskiller.jfairy.producer.company.locale.sv.SvVATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.locale.zh.ZhAddressProvider;
import com.devskiller.jfairy.producer.person.locale.zh.ZhNationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.person.locale.zh.ZhPassportNumberProvider;
import com.devskiller.jfairy.producer.company.locale.zh.ZhVATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.util.LanguageCode;

/**
 * Factory for creating locale-specific provider implementations
 */
class LocaleSpecificProvidersFactory {

	private static final Logger LOG = LoggerFactory.getLogger(LocaleSpecificProvidersFactory.class);

	private LocaleSpecificProvidersFactory() {
	}

	static LocaleSpecificProviders createProvidersForLocale(Locale locale,
	                                                        DataMaster dataMaster,
	                                                        BaseProducer baseProducer,
	                                                        DateProducer dateProducer) {
		LanguageCode code;
		try {
			code = LanguageCode.valueOf(locale.getLanguage().toUpperCase());
		} catch (IllegalArgumentException e) {
			LOG.warn("Unknown locale {}", locale);
			code = LanguageCode.EN;
		}

		return switch (code) {
			case PL -> createPlProviders(dataMaster, baseProducer, dateProducer);
			case EN -> createEnProviders(dataMaster, baseProducer, dateProducer);
			case ES -> createEsProviders(dataMaster, baseProducer, dateProducer);
			case FR -> createFrProviders(dataMaster, baseProducer, dateProducer);
			case IT -> createEnProviders(dataMaster, baseProducer, dateProducer);
			case SV -> createSvProviders(dataMaster, baseProducer, dateProducer);
			case ZH -> createZhProviders(dataMaster, baseProducer, dateProducer);
			case DE -> createDeProviders(dataMaster, baseProducer, dateProducer);
			case KA -> createKaProviders(dataMaster, baseProducer, dateProducer);
		};
	}

	private static LocaleSpecificProviders createPlProviders(DataMaster dataMaster,
	                                                         BaseProducer baseProducer,
	                                                         DateProducer dateProducer) {
		return new LocaleSpecificProviders(
				new PlNationalIdentificationNumberFactory(baseProducer, dateProducer),
				new PlNationalIdentityCardNumberProvider(dateProducer, baseProducer),
				new PlVATIdentificationNumberProvider(baseProducer),
				new PlAddressProvider(dataMaster, baseProducer),
				new PlPassportNumberProvider()
		);
	}

	private static LocaleSpecificProviders createEnProviders(DataMaster dataMaster,
	                                                         BaseProducer baseProducer,
	                                                         DateProducer dateProducer) {
		return new LocaleSpecificProviders(
				new NoNationalIdentificationNumberFactory(),
				new EnNationalIdentityCardNumberProvider(baseProducer),
				new EnVATIdentificationNumberProvider(baseProducer),
				new EnAddressProvider(dataMaster, baseProducer),
				new EnPassportNumberProvider()
		);
	}

	private static LocaleSpecificProviders createFrProviders(DataMaster dataMaster,
	                                                         BaseProducer baseProducer,
	                                                         DateProducer dateProducer) {
		return new LocaleSpecificProviders(
				new NoNationalIdentificationNumberFactory(),
				new EnNationalIdentityCardNumberProvider(baseProducer),
				new EnVATIdentificationNumberProvider(baseProducer),
				new FrAddressProvider(dataMaster, baseProducer),
				new EnPassportNumberProvider()
		);
	}

	private static LocaleSpecificProviders createEsProviders(DataMaster dataMaster,
	                                                         BaseProducer baseProducer,
	                                                         DateProducer dateProducer) {
		return new LocaleSpecificProviders(
				new NoNationalIdentificationNumberFactory(),
				new EsNationalIdentityCardNumberProvider(),
				new EsVATIdentificationNumberProvider(),
				new EsAddressProvider(dataMaster, baseProducer),
				new EsPassportNumberProvider()
		);
	}

	private static LocaleSpecificProviders createSvProviders(DataMaster dataMaster,
	                                                         BaseProducer baseProducer,
	                                                         DateProducer dateProducer) {
		NationalIdentificationNumberFactory nationalIdFactory = new SvNationalIdentificationNumberFactory(baseProducer, dateProducer);
		return new LocaleSpecificProviders(
				nationalIdFactory,
				new SvNationalIdentityCardNumberProvider(dateProducer, baseProducer),
				new SvVATIdentificationNumberProvider(baseProducer, dateProducer, nationalIdFactory),
				new SvAddressProvider(dataMaster, baseProducer),
				new SvPassportNumberProvider()
		);
	}

	private static LocaleSpecificProviders createZhProviders(DataMaster dataMaster,
	                                                         BaseProducer baseProducer,
	                                                         DateProducer dateProducer) {
		return new LocaleSpecificProviders(
				new NoNationalIdentificationNumberFactory(),
				new ZhNationalIdentityCardNumberProvider(baseProducer),
				new ZhVATIdentificationNumberProvider(),
				new ZhAddressProvider(dataMaster, baseProducer),
				new ZhPassportNumberProvider()
		);
	}

	private static LocaleSpecificProviders createDeProviders(DataMaster dataMaster,
	                                                         BaseProducer baseProducer,
	                                                         DateProducer dateProducer) {
		return new LocaleSpecificProviders(
				new NoNationalIdentificationNumberFactory(),
				new DeNationalIdentityCardNumberProvider(baseProducer),
				new DeVATIdentificationNumberProvider(),
				new DeAddressProvider(dataMaster, baseProducer),
				new DePassportNumberProvider(baseProducer)
		);
	}

	private static LocaleSpecificProviders createKaProviders(DataMaster dataMaster,
	                                                         BaseProducer baseProducer,
	                                                         DateProducer dateProducer) {
		return new LocaleSpecificProviders(
				new NoNationalIdentificationNumberFactory(),
				new KaNationalIdentityCardNumberProvider(baseProducer),
				new KaVATIdentificationNumberProvider(baseProducer),
				new KaAddressProvider(dataMaster, baseProducer),
				new KaPassportNumberProvider(baseProducer)
		);
	}
}
