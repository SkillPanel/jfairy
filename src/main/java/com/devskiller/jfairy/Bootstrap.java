package com.devskiller.jfairy;

import java.io.IOException;
import java.util.Locale;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.data.MapBasedDataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.RandomGenerator;
import com.devskiller.jfairy.producer.TimeProvider;
import com.devskiller.jfairy.producer.company.CompanyFactory;
import com.devskiller.jfairy.producer.company.CompanyFactoryImpl;
import com.devskiller.jfairy.producer.net.IPNumberProducer;
import com.devskiller.jfairy.producer.payment.CreditCardProvider;
import com.devskiller.jfairy.producer.payment.IBANFactory;
import com.devskiller.jfairy.producer.payment.IBANFactoryImpl;
import com.devskiller.jfairy.producer.person.PersonFactory;
import com.devskiller.jfairy.producer.person.PersonFactoryImpl;
import com.devskiller.jfairy.producer.text.TextProducerInternal;

/**
 * <p>Using a {@link #builder()}, you can configure the following fields:</p>
 * <ul>
 * <li><tt>locale</tt>: Specifies the locale for the random data file.</li>
 * <li><tt>filePrefix</tt>: Specifies the file prefix.
 * (So if you specify "jfairy" here and English for Locale, the data file will be
 * "jfairy_en.yml" under the classpath.)
 * </li>
 * <li><tt>random</tt>: The Random object to use.</li>
 * <li><tt>randomSeed</tt>: A specific random seed to use. Use this if you want the resulting
 * data to be <strong>deterministic</strong> based on it, such as if you want the same test
 * ID in a database to always result in the same fake name.
 * </li>
 * </ul>
 * Obviously, don't set both <tt>random</tt> and <tt>randomSeed</tt>, only the last one you set will
 * actually take effect.
 *
 * @author Jakub Kubrynski
 * @author Olga Maciaszek-Sharma
 */
public class Bootstrap {

	private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

	private static final String DATA_FILE_PREFIX = "jfairy";

	public static Fairy createFairy(DataMaster dataMaster, Locale locale, RandomGenerator randomGenerator) {
		// Create base components
		BaseProducer baseProducer = new BaseProducer(randomGenerator);
		TimeProvider timeProvider = new TimeProvider();
		DateProducer dateProducer = new DateProducer(baseProducer, timeProvider);
		
		// Create locale-specific providers
		LocaleSpecificProviders localeProviders = LocaleSpecificProvidersFactory.createProvidersForLocale(
				locale, dataMaster, baseProducer, dateProducer);
		
		// Create company factory first (needed by PersonFactory)
		CompanyFactory companyFactory = new CompanyFactoryImpl(
				baseProducer, dataMaster, localeProviders.vatIdentificationNumberProvider()
		);

		// Create person factory
		PersonFactory personFactory = new PersonFactoryImpl(
				dataMaster, dateProducer, baseProducer,
				localeProviders.addressProvider(),
				localeProviders.nationalIdentificationNumberFactory(),
				localeProviders.nationalIdentityCardNumberProvider(),
				localeProviders.passportNumberProvider(),
				timeProvider,
				companyFactory
		);
		
		IBANFactory ibanFactory = new IBANFactoryImpl(baseProducer, dataMaster);
		
		// Create other producers
		CreditCardProvider creditCardProvider = new CreditCardProvider(dataMaster, baseProducer, dateProducer);
		TextProducerInternal textProducerInternal = new TextProducerInternal(dataMaster, baseProducer);
		IPNumberProducer ipNumberProducer = new IPNumberProducer(baseProducer);
		
		// Create fairy factory
		FairyFactory fairyFactory = new FairyFactoryImpl(
				textProducerInternal, baseProducer, personFactory,
				ipNumberProducer, dateProducer, creditCardProvider,
				companyFactory, ibanFactory
		);
		
		return fairyFactory.createFairy();
	}


	private static void fillDefaultDataMaster(MapBasedDataMaster dataMaster, Locale locale, String filePrefix) {
		try {
			dataMaster.readResources(filePrefix + ".yml");
			dataMaster.readResources(filePrefix + "_" + locale.getLanguage() + ".yml");
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Creates a Builder that will let you configure a Fairy's fields one by one.
	 *
	 * @return a Builder instance.
	 */
	public static Builder builder() {
		return new Builder();
	}


	/**
	 * Use this factory method to create dataset containing default jfairy.yml and jfairy_{langCode}.yml files
	 * merged with custom files with the same name
	 *
	 * @return Fairy instance
	 */
	public static Fairy create() {
		return builder().build();
	}

	/**
	 * Use this factory method to create dataset containing default jfairy.yml and jfairy_{langCode}.yml files
	 * merged with custom files with the same name
	 *
	 * @param locale will be used to assess langCode for data file
	 * @return Fairy instance
	 */
	public static Fairy create(Locale locale) {
		return builder().withLocale(locale).build();
	}

	/**
	 * Use this factory method to create your own dataset overriding bundled one
	 *
	 * @param locale         will be used to assess langCode for data file
	 * @param dataFilePrefix prefix of the data file - final pattern will be jfairy.yml and dataFilePrefix_{langCode}.yml
	 * @return Fairy instance
	 */
	public static Fairy create(Locale locale, String dataFilePrefix) {
		return builder().withLocale(locale)
				.withFilePrefix(dataFilePrefix)
				.build();
	}


	public static Fairy create(Supplier<DataMaster> dataMaster, Locale locale) {
		return builder().withDataMasterProvider(dataMaster).withLocale(locale).build();
	}

	
	public static class Builder {

		private Locale locale = Locale.ENGLISH;
		private String filePrefix = DATA_FILE_PREFIX;
		private RandomGenerator randomGenerator = new RandomGenerator();
		private DataMaster dataMaster;


		private MapBasedDataMaster getDefaultDataMaster(BaseProducer baseProducer) {
			return new MapBasedDataMaster(baseProducer);
		}

		private Builder() {

		}

		/**
		 * Sets the locale for the resulting Fairy.
		 *
		 * @param locale The Locale to set.
		 * @return the same Builder (for chaining).
		 */
		public Builder withLocale(Locale locale) {
			this.locale = locale;
			return this;
		}

		/**
		 * Sets the data file prefix for the resulting Fairy.
		 *
		 * @param filePrefix The prefix of the file (such as "jfairy" for "jfairy_en.yml").
		 * @return the same Builder (for chaining).
		 */
		public Builder withFilePrefix(String filePrefix) {
			this.filePrefix = filePrefix;
			return this;
		}

		/**
		 * Sets the random seed to use to pick things randomly. (If you set this, you will always
		 * get the same result when you generate things.)
		 *
		 * @param randomSeed The random seed to use.
		 * @return the same Builder (for chaining).
		 */
		public Builder withRandomSeed(int randomSeed) {
			this.randomGenerator = new RandomGenerator(randomSeed);
			return this;
		}

		/**
		 * Sets a custom DataMaster implementation.
		 *
		 * @param dataMasterProvider The DataMaster supplier to use.
		 * @return the same Builder (for chaining).
		 */
		public Builder withDataMasterProvider(Supplier<DataMaster> dataMasterProvider) {
			this.dataMaster = dataMasterProvider.get();
			return this;
		}


		/**
		 * Returns the completed Fairy.
		 *
		 * @return Fairy instance
		 */
		public Fairy build() {
			if (dataMaster == null) {
				BaseProducer baseProducer = new BaseProducer(randomGenerator);
				dataMaster = getDefaultDataMaster(baseProducer);
				fillDefaultDataMaster((MapBasedDataMaster) dataMaster, locale, filePrefix);
			}
			return createFairy(dataMaster, locale, randomGenerator);
		}
	}


}
