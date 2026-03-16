package com.devskiller.jfairy.producer

import org.apache.commons.validator.routines.DomainValidator
import org.apache.commons.validator.routines.EmailValidator
import org.apache.commons.validator.routines.UrlValidator
import spock.lang.Specification

import com.devskiller.jfairy.Bootstrap
import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.company.Company

import static com.devskiller.jfairy.producer.company.CompanyProperties.CompanyProperty.withDomain
import static com.devskiller.jfairy.producer.company.CompanyProperties.CompanyProperty.withEmail
import static com.devskiller.jfairy.producer.company.CompanyProperties.CompanyProperty.withName
import static com.devskiller.jfairy.producer.company.CompanyProperties.CompanyProperty.withVatIdentificationNumber

/**
 * @author Codearte
 * @since 2013-10-07
 */
class CompanySpec extends Specification {

	private EmailValidator emailValidator = EmailValidator.getInstance()
	private UrlValidator urlValidator = UrlValidator.getInstance()
	private DomainValidator domainValidator = DomainValidator.getInstance()
	private Fairy fairy = Bootstrap.create()

	def "should instantiate Company producer"() {
		when:
			Company company = fairy.company()
		then:
			company
	}

	def "should be sure that data exists"() {
		when:
			Company company = fairy.company()
		then:
			company.name
			domainValidator.isValid(company.domain)
			emailValidator.isValid(company.email)
			urlValidator.isValid(company.url)
			company.vatIdentificationNumber
	}

	def "withName should create company with specific name"() {
		when:
			Company company = fairy.company(withName("Specific Name"))
		then:
			company.name == "Specific Name"
	}

	def "withDomain should create company with specific domain"() {
		when:
			Company company = fairy.company(withDomain("specificdomain.com"))
		then:
			company.domain == "specificdomain.com"
	}

	def "withEmail should create company with specific email"() {
		when:
			Company company = fairy.company(withEmail("specificemail"))
		then:
			company.email.startsWith("specificemail@")
	}

	def "withVatIdentificationNumber should create company with specific vat identification number"() {
		when:
			Company company = fairy.company(withVatIdentificationNumber("specificvatidentificationnumber"))
		then:
			company.vatIdentificationNumber == "specificvatidentificationnumber"
	}
}
