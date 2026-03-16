package com.devskiller.jfairy.producer.person

import spock.lang.Specification

import com.devskiller.jfairy.producer.company.Company

class CompanyEmailProviderSpec extends Specification {

	def "should remove spaces from firstName and lastName and lowercase letters in company email"() {
		given:
			Company company = new Company(null, "companymail.com", null, null)
			CompanyEmailProvider companyEmailProvider = new CompanyEmailProvider("Emilie Agneta", "Vojnov Allerstrand", company)

		when:
			String email = companyEmailProvider.get()

		then:
			email == "emilie.agneta.vojnov.allerstrand@companymail.com"
	}

	def "should strip accents from company email"() {
		given:
			Company company = new Company(null, "åäöéáąćęłńśóźż.com", null, null)
			CompanyEmailProvider companyEmailProvider = new CompanyEmailProvider("åäöéáąćęłńśóźż", "åäöéáąćęłńśóźż", company)

		when:
			String email = companyEmailProvider.get()

		then:
			email == "aaoeaacelnsozz.aaoeaacelnsozz@aaoeaacelnsozz.com"
	}

	def "should strip sharp s from company email"() {
		given:
			Company company = new Company(null, "companymail.com", null, null)
			CompanyEmailProvider companyEmailProvider = new CompanyEmailProvider("Thieß", "Weißmann", company)

		when:
			String email = companyEmailProvider.get()

		then:
			email == "thiess.weissmann@companymail.com"
	}
}
