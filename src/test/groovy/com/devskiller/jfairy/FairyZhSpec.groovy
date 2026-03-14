package com.devskiller.jfairy

import spock.lang.Specification

import com.devskiller.jfairy.producer.company.Company
import com.devskiller.jfairy.producer.person.Person

/**
 *
 * com.devskiller.jfairy.FairyZhSpec
 * @author lhfcws
 * @since 2017/3/2
 */
class FairyZhSpec extends Specification {
	private final int SEED = 1
	private Fairy fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.CHINA).build()

	/********************
	 * Person
	 */
	def "Should create Chinese name"() {
		when:
		Person person = fairy.person()
		then:
		person.fullName == '国富 钱'
	}

	def "Should create Chinese id card"() {
		when:
		Person person = fairy.person()
		then:
		person.nationalIdentityCardNumber.length() == 18
	}

	def "Should create empty since Chinese id number is the only citizen id in China"() {
		when:
		Person person = fairy.person()
		then:
		person.nationalIdentificationNumber.length() == 0
	}

	def "Should create Chinese address"() {
		when:
		Person person = fairy.person()
		then:
		person.address.addressLine1 == '台北市朝阳大街67号'
	}

	def "Should create Chinese city"() {
		when:
		Person person = fairy.person()
		then:
		person.address.city == '台北'
	}

	/********************
	 * Company
	 */
	def "Should create Chinese company name"() {
		when:
		Company company = fairy.company()
		then:
		company.name == "后海金融"
	}

	def "Should create Chinese company url"() {
		when:
		Company company = fairy.company()
		then:
		company.url == "http://www.540e6d7791.org"
	}

	def "Should create Chinese company vat"() {
		when:
		Company company = fairy.company()
		then:
		company.vatIdentificationNumber.length() == 15
	}
}
