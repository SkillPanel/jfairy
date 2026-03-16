package com.devskiller.jfairy.producer.payment

import org.iban4j.IbanUtil
import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.data.DataMaster
import com.devskiller.jfairy.data.MapBasedDataMaster
import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

class IBANSpec extends Specification {

	private DataMaster dataMaster
	private BaseProducer baseProducer

	def setup() {
		baseProducer = new BaseProducer(new RandomGenerator())
		dataMaster = new MapBasedDataMaster(baseProducer)
		dataMaster.readResources("jfairy_pl.yml")
	}

	/**
	 * Austria	20	16n
	 * ATkk bbbb bccc cccc cccc
	 *
	 * b = National bank code
	 c = Account number
	 */
	def "should return valid iban"() {
		when:
			IBANProvider iban = new DefaultIBANProvider(
				baseProducer,
				dataMaster,
				IBANProperties.accountNumber("00234573201"),
				IBANProperties.country("AT")
			)

		then:
			IbanUtil.validate(iban.get().ibanNumber)
	}

	/**
	 * Poland	28	24n	PLkk bbbs sssx cccc cccc cccc cccc	b = National bank code
	 s = Branch code
	 x = National check digit
	 c = Account number,

	 PLkk bbbssssx cccccccccccccccc
	 PL60 11401010 1111000234573201
	 */
	def "should return valid polish iban"() {
		when:
			IBANProvider iban = new DefaultIBANProvider(baseProducer, dataMaster)
		then:
			IbanUtil.validate(iban.get().ibanNumber)
	}

	def "should be usable directly from Fairy"() {
		when:
			String number = Fairy.create().iban(IBANProperties.country("PL")).ibanNumber
		then:
			number.startsWith('PL')
	}

	def "should ignore countries not supporting iban"() {
		when:
			def iban = Fairy.create().iban(IBANProperties.country("US"))
		then:
			iban == null
	}

	def "should set proper country for specified language"() {
		when:
			String number = Fairy.create().iban(IBANProperties.language("DE")).ibanNumber
		then:
			number.startsWith('DE')
	}

	def "should set proper country for according to selected language"() {
		when:
			String number = Fairy.create(Locale.of('SV')).iban().ibanNumber
		then:
			number.startsWith('SE')
	}
}
