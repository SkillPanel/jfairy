package com.devskiller.jfairy

import spock.lang.Specification

import com.devskiller.jfairy.data.MapBasedDataMaster
import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.DateProducer
import com.devskiller.jfairy.producer.person.locale.de.DeAddressProvider
import com.devskiller.jfairy.producer.person.locale.en.EnAddressProvider
import com.devskiller.jfairy.producer.person.locale.es.EsAddressProvider
import com.devskiller.jfairy.producer.person.locale.ka.KaAddressProvider
import com.devskiller.jfairy.producer.person.locale.pl.PlAddressProvider
import com.devskiller.jfairy.producer.person.locale.sv.SvAddressProvider
import com.devskiller.jfairy.producer.person.locale.zh.ZhAddressProvider

class FairyModuleSpec extends Specification {

	private MapBasedDataMaster mapBasedDataMaster = Stub(MapBasedDataMaster)
	private BaseProducer baseProducer = Stub(BaseProducer)
	private DateProducer dateProducer = Stub(DateProducer)

	def "should create appropriate locale-specific providers for locale"() {
		when:
			LocaleSpecificProviders providers = LocaleSpecificProvidersFactory.createProvidersForLocale(
				Locale.forLanguageTag(locale), mapBasedDataMaster, baseProducer, dateProducer)

		then:
			providers.addressProvider.getClass() == expectedAddressProvider

		where:
			locale | expectedAddressProvider
			"en"   | EnAddressProvider.class
			"pl"   | PlAddressProvider.class
			"es"   | EsAddressProvider.class
			"de"   | DeAddressProvider.class
			"sv"   | SvAddressProvider.class
			"ka"   | KaAddressProvider.class
			"zh"   | ZhAddressProvider.class
	}

	def "should fall back to English providers for unknown locale"() {
		when:
			LocaleSpecificProviders providers = LocaleSpecificProvidersFactory.createProvidersForLocale(
				Locale.forLanguageTag("xx"), mapBasedDataMaster, baseProducer, dateProducer)

		then:
			providers.addressProvider.getClass() == EnAddressProvider.class
	}
}
