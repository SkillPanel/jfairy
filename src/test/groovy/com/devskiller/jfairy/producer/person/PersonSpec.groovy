package com.devskiller.jfairy.producer.person

import java.time.LocalDate
import java.time.Period

import org.apache.commons.validator.routines.EmailValidator
import spock.lang.Ignore
import spock.lang.Specification

import com.devskiller.jfairy.Bootstrap
import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.company.Company
import com.devskiller.jfairy.producer.person.locale.en.EnAddress
import com.devskiller.jfairy.producer.util.LanguageCode

import static com.devskiller.jfairy.producer.person.PersonProperties.ageBetween
import static com.devskiller.jfairy.producer.person.PersonProperties.female
import static com.devskiller.jfairy.producer.person.PersonProperties.male
import static com.devskiller.jfairy.producer.person.PersonProperties.maxAge
import static com.devskiller.jfairy.producer.person.PersonProperties.minAge
import static com.devskiller.jfairy.producer.person.PersonProperties.mobileTelephoneFormat
import static com.devskiller.jfairy.producer.person.PersonProperties.telephoneFormat
import static com.devskiller.jfairy.producer.person.PersonProperties.withAddress
import static com.devskiller.jfairy.producer.person.PersonProperties.withAge
import static com.devskiller.jfairy.producer.person.PersonProperties.withCompany
import static com.devskiller.jfairy.producer.person.PersonProperties.withCompanyEmail
import static com.devskiller.jfairy.producer.person.PersonProperties.withDateOfBirth
import static com.devskiller.jfairy.producer.person.PersonProperties.withEmail
import static com.devskiller.jfairy.producer.person.PersonProperties.withFirstName
import static com.devskiller.jfairy.producer.person.PersonProperties.withLastName
import static com.devskiller.jfairy.producer.person.PersonProperties.withMiddleName
import static com.devskiller.jfairy.producer.person.PersonProperties.withNationalIdentificationNumber
import static com.devskiller.jfairy.producer.person.PersonProperties.withNationalIdentityCardNumber
import static com.devskiller.jfairy.producer.person.PersonProperties.withJobTitle
import static com.devskiller.jfairy.producer.person.PersonProperties.withPassportNumber
import static com.devskiller.jfairy.producer.person.PersonProperties.withPassword
import static com.devskiller.jfairy.producer.person.PersonProperties.withTelephoneNumber
import static com.devskiller.jfairy.producer.person.PersonProperties.withMobileTelephoneNumber
import static com.devskiller.jfairy.producer.person.PersonProperties.withUsername

class PersonSpec extends Specification {

	private EmailValidator emailValidator = EmailValidator.getInstance()
	private Fairy fairy = Bootstrap.create()

	def setup() {
		Bootstrap.create()
	}

	def "should instantiate PersonProducer producer with person"() {
		when:
			Person person = fairy.person()
		then:
			person instanceof Person
	}

	def "should be sure that fullName is proper"() {
		when:
			Person person = fairy.person()
		then:
			"${person.firstName} ${person.lastName}" == person.fullName
	}

	def "second generated name should be different"() {
		expect:
			fairy.person().firstName != fairy.person().firstName
	}

	def "should be sure that data exists"() {
		when:
			Person person = fairy.person()
		then:
			person.firstName
			person.lastName
			person.fullName
			person.email
			person.male || person.female
			person.nationalIdentityCardNumber
			person.address

			emailValidator.isValid(person.email)
	}

	def "should create female"() {
		when:
			Person person = fairy.person(female())
		then:
			person.female
	}

	def "should create male"() {
		when:
			Person person = fairy.person(male())
		then:
			person.male
	}

	def "should create person younger than 3 years"() {
		when:
			Person person = fairy.person(maxAge(3))
		then:
			person.age <= 3
	}

	def "should create nationality"() {
		when:
			Person person = fairy.person()
		then:
			Country.findCountryForLanguage(LanguageCode.EN)*.getCode().contains(person.nationality.code)
	}

	@Ignore("Properties are not cleaned")
	def "should create person older than 98 years"() {
		when:
			Person person = Bootstrap.create().person(minAge(99))
		then:
			person.age > 98
	}

	@Ignore("Properties are not cleaned")
	def "should create person older than 10 years and younger than 10 years"() {
		when:
			Person person = Bootstrap.create().person(minAge(10), maxAge(10))
		then:
			person.age == 10
	}

	def "birth date and age should be related"() {
		when:
			Person person = fairy.person(ageBetween(32, 32))
		then:
			person.age == 32
			Period period = Period.between(person.getDateOfBirth(), LocalDate.now())
			period.years == 32
	}

	def "should create telephone number"() {
		when:
			Person person = fairy.person()
		then:
			person.telephoneNumber
	}

	def "should create telephone number in defined format"() {
		when:
			Person person = fairy.person(telephoneFormat("###--###"))
		then:
			person.telephoneNumber ==~ /\d\d\d--\d\d\d/
	}

	def "should create mobile telephone number"() {
		when:
			Person person = fairy.person()
		then:
			person.mobileTelephoneNumber
	}

	def "should create mobile telephone number in defined format"() {
		when:
			Person person = fairy.person(mobileTelephoneFormat("###--###"))
		then:
			person.mobileTelephoneNumber ==~ /\d\d\d--\d\d\d/
	}

	def "should create birth date"() {
		when:
			Person person = fairy.person()
		then:
			person.dateOfBirth.isBefore(LocalDate.now())
	}

	def "should create age"() {
		when:
			Person person = fairy.person()
		then:
			person.age
	}

	def "should create company email"() {
		given:
			Fairy fairy = fairy

		when:
			Person person = fairy.person()

		then:
			person.companyEmail
			emailValidator.isValid(person.companyEmail)
	}

	def "should create address"() {
		given:
			Person person = fairy.person()
		when:
			Address address = person.address
		then:
			address
	}

	def "should create address postal code"() {
		given:
			Person person = fairy.person()
		when:
			String postalCode = person.address.postalCode
		then:
			postalCode
	}

	def "should create address city"() {
		given:
			Person person = fairy.person()
		when:
			String city = person.address.city
		then:
			city
	}

	def "should create street address"() {
		given:
			Person person = fairy.person()
		when:
			Address address = person.address
		then:
			address.street
			address.streetNumber.isNumber()
			(address.apartmentNumber.isNumber() || address.apartmentNumber == "")
	}

	def "should generate middle name only sometimes"() {
		given:
			List<Person> persons = []
			(1..100).each {persons.add(fairy.person())}
		when:
			List<Person> allWithoutMiddleName = persons.findAll {p -> p.middleName.isEmpty()}
			List<Person> allWithMiddleName = persons.findAll {p -> !p.middleName.isEmpty()}
		then:
			allWithoutMiddleName.size() > 0
			allWithMiddleName.size() > 0
	}

	def "should generate apartment number only sometimes"() {
		given:
			List<Person> persons = []
			(1..50).each {persons.add(fairy.person())}
		when:
			List<Person> allWithoutApartmentNumber = persons.findAll {p -> p.address.apartmentNumber.isEmpty()}
			List<Person> allWithApartmentNumber = persons.findAll {p -> !p.address.apartmentNumber.isEmpty()}
		then:
			allWithoutApartmentNumber.size() > 0
			allWithApartmentNumber.size() > 0
	}

	def "should create passport number"() {
		given:
			Person person = fairy.person()
		expect:
			person.passportNumber
	}

	def "withFirstName should create person with specific first name"() {
		when:
			Person person = fairy.person(withFirstName("Specificfirstname"))
		then:
			person.firstName == "Specificfirstname"
	}

	def "withMiddleName should create person with specific middle name"() {
		when:
			Person person = fairy.person(withMiddleName("Specificmiddlename"))
		then:
			person.middleName == "Specificmiddlename"
	}

	def "withLastName should create person with specific last name"() {
		when:
			Person person = fairy.person(withLastName("Specificlastname"))
		then:
			person.lastName == "Specificlastname"
	}

	def "withEmail should create person with specific email"() {
		when:
			Person person = fairy.person(withEmail("specificemail@gmail.com"))
		then:
			person.email == "specificemail@gmail.com"
	}

	def "withUsername should create person with specific username"() {
		when:
			Person person = fairy.person(withUsername("specificusername"))
		then:
			person.username == "specificusername"
	}

	def "withTelephoneNumber should create person with specific telephoneNumber"() {
		when:
			Person person = fairy.person(withTelephoneNumber("01234556789"))
		then:
			person.telephoneNumber == "01234556789"
	}

	def "withTelephoneNumberFormat and telephoneFormat used together should create person with specific telephoneNumber"() {
		when:
			Person person = fairy.person(telephoneFormat("###--###"), withTelephoneNumber("01234556789"))
		then:
			person.telephoneNumber == "01234556789"
	}

	def "withMobileTelephoneNumber should create person with specific telephoneNumber"() {
		when:
			Person person = fairy.person(withMobileTelephoneNumber("01234556789"))
		then:
			person.telephoneNumber == "01234556789"
	}

	def "withMobileTelephoneNumberFormat and mobileTelephoneFormat used together should create person with specific telephoneNumber"() {
		when:
			Person person = fairy.person(mobileTelephoneFormat("###--###"), withMobileTelephoneNumber("01234556789"))
		then:
			person.telephoneNumber == "01234556789"
	}

	def "withDateOfBirth should create person with specific date of birth"() {
		when:
			Person person = fairy.person(withDateOfBirth(LocalDate.parse("2000-01-01")))
		then:
			person.dateOfBirth == LocalDate.parse("2000-01-01")
	}

	def "withAge should create person with specific age"() {
		when:
			Person person = fairy.person(withAge(0))
		then:
			person.age == 0
	}

	def "withPassword should create person with specific password"() {
		when:
			Person person = fairy.person(withPassword("specificpassword"))
		then:
			person.password == "specificpassword"
	}

	def "withCompanyEmail should create person with specific company email"() {
		when:
			Person person = fairy.person(withCompanyEmail("specificcompanyemail@company.com"))
		then:
			person.companyEmail == "specificcompanyemail@company.com"
	}

	def "withNationalIdentityCardNumber should create person with specific national identity card number"() {
		when:
			Person person = fairy.person(withNationalIdentityCardNumber("SpecificNationalIdentityCardNumber"))
		then:
			person.nationalIdentityCardNumber == "SpecificNationalIdentityCardNumber"
	}

	def "withNationalIdentificationNumber should create person with specific national identification number"() {
		when:
			Person person = fairy.person(withNationalIdentificationNumber("SpecificNationalIdentificationNumber"))
		then:
			person.nationalIdentificationNumber == "SpecificNationalIdentificationNumber"
	}

	def "withPassportNumber should create person with specific passport number"() {
		when:
			Person person = fairy.person(withPassportNumber("SpecificPassportNumber"))
		then:
			person.passportNumber == "SpecificPassportNumber"
	}

	def "should create person with job title"() {
		given:
			Person person = fairy.person()
		expect:
			person.jobTitle
	}

	def "withJobTitle should create person with specific job title"() {
		when:
			Person person = fairy.person(withJobTitle("Specific Job Title"))
		then:
			person.jobTitle == "Specific Job Title"
	}

	def "withCompany should create person with specific company"() {
		when:
			Company specificCompany = new Company("Company name", "domain.com", "company@emial.com", "VATNO");
			Person person = fairy.person(withCompany(specificCompany))
		then:
			person.getCompany() == specificCompany
	}

	def "withAddress should create person with specific address"() {
		when:
			EnAddress specificAddress = new EnAddress("POSTALCODE", "City", "Streetname", "123", "10B");
			Person person = fairy.person(withAddress(specificAddress))
		then:
			person.getAddress() == specificAddress
	}

}
