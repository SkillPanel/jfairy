package com.devskiller.jfairy.producer.person;

import java.time.LocalDate;

import com.devskiller.jfairy.producer.company.Company;

import static com.devskiller.jfairy.producer.person.Person.Sex.FEMALE;
import static com.devskiller.jfairy.producer.person.Person.Sex.MALE;

public class Person {

	public enum Sex {
		MALE, FEMALE
	}

	private final Address address;
	private final String firstName;
	private final String middleName;
	private final String lastName;
	private final String email;
	private final String username;
	private final String password;
	private final Sex sex;
	private final String telephoneNumber;
	private final String mobileTelephoneNumber;
	private final LocalDate dateOfBirth;
	private final Integer age;
	private final Company company;
	private final String companyEmail;
	private final String nationalIdentityCardNumber;
	private final String nationalIdentificationNumber;
	private final String passportNumber;
	private final String jobTitle;
	private final Country nationality;

	public Person(String firstName, String middleName, String lastName, Address address, String email, String username,
	              String password, Sex sex, String telephoneNumber, String mobileTelephoneNumber,
				  LocalDate dateOfBirth, Integer age, String nationalIdentityCardNumber,
				  String nationalIdentificationNumber, String passportNumber, Company company, String companyEmail,
				  String jobTitle, Country nationality) {
		this.nationalIdentityCardNumber = nationalIdentityCardNumber;
		this.address = address;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.sex = sex;
		this.telephoneNumber = telephoneNumber;
		this.mobileTelephoneNumber = mobileTelephoneNumber;
		this.dateOfBirth = dateOfBirth;
		this.age = age;
		this.nationalIdentificationNumber = nationalIdentificationNumber;
		this.company = company;
		this.companyEmail = companyEmail;
		this.passportNumber = passportNumber;
		this.jobTitle = jobTitle;
		this.nationality = nationality;
	}

	public String getNationalIdentificationNumber() {
		return nationalIdentificationNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public boolean isMale() {
		return sex == MALE;
	}

	public boolean isFemale() {
		return sex == FEMALE;
	}

	public Sex getSex() {
		return sex;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public String getMobileTelephoneNumber() {
		return mobileTelephoneNumber;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public int getAge() {
		return age;
	}

	public String getNationalIdentityCardNumber() {
		return nationalIdentityCardNumber;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public Address getAddress() {
		return address;
	}

	public Company getCompany() {
		return company;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public Country getNationality() {
		return nationality;
	}
}
