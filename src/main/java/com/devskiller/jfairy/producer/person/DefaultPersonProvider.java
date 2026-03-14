package com.devskiller.jfairy.producer.person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.TimeProvider;
import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.company.CompanyFactory;

import static com.devskiller.jfairy.producer.util.StringUtils.lowerCase;
import static com.devskiller.jfairy.producer.util.StringUtils.latinize;

public class DefaultPersonProvider implements PersonProvider {

	protected Person.Sex sex;
	protected String telephoneNumberFormat;
	protected String mobileTelephoneNumberFormat;
	protected Integer age;
	protected LocalDate dateOfBirth;
	protected Company company;
	protected Address address;
	protected String firstName;
	protected String middleName;
	protected String lastName;
	protected String email;
	protected String username;
	protected String telephoneNumber;
	protected String mobileTelephoneNumber;
	protected String password;
	protected String companyEmail;
	protected String nationalIdentityCardNumber;
	protected String nationalIdentificationNumber;
	protected String passportNumber;
	protected String jobTitle;
	protected Country nationality;

	protected final DataMaster dataMaster;
	protected final DateProducer dateProducer;
	protected final BaseProducer baseProducer;
	protected final NationalIdentificationNumberFactory nationalIdentificationNumberFactory;
	protected final NationalIdentityCardNumberProvider nationalIdentityCardNumberProvider;
	protected final AddressProvider addressProvider;
	protected final CompanyFactory companyFactory;
	
	protected final TimeProvider timeProvider;
	protected final PassportNumberProvider passportNumberProvider;


	public DefaultPersonProvider(DataMaster dataMaster,
	                             DateProducer dateProducer,
	                             BaseProducer baseProducer,
	                             AddressProvider addressProvider,
	                             NationalIdentificationNumberFactory nationalIdentificationNumberFactory,
	                             NationalIdentityCardNumberProvider nationalIdentityCardNumberProvider,
	                             PassportNumberProvider passportNumberProvider,
	                             TimeProvider timeProvider,
	                             CompanyFactory companyFactory,
	                             PersonProperties.PersonProperty... personProperties) {
		this.dataMaster = dataMaster;
		this.dateProducer = dateProducer;
		this.baseProducer = baseProducer;
		this.nationalIdentificationNumberFactory = nationalIdentificationNumberFactory;
		this.nationalIdentityCardNumberProvider = nationalIdentityCardNumberProvider;
		this.addressProvider = addressProvider;
		this.passportNumberProvider = passportNumberProvider;
		this.companyFactory = companyFactory;
		this.timeProvider = timeProvider;

		for (PersonProperties.PersonProperty personProperty : personProperties) {
			personProperty.apply(this, baseProducer);
		}
	}

	@Override
	public Person get() {

		generateSex();
		generateCompany();
		generateFirstName();
		generateMiddleName();
		generateLastName();
		generateEmail();
		generateUsername();
		generateTelephoneNumber();
		generateAge();
		generateDateOfBirth();
		generateCompanyEmail();
		generatePassword();
		generateNationalIdentityCardNumber();
		generateNationalIdentificationNumber();
		generatePassportNumber();
		generateAddress();
		generateJobTitle();
		generateNationality();
		generateMobileTelephoneNumber();

		return new Person(firstName, middleName, lastName, address, email,
			username, password, sex, telephoneNumber, mobileTelephoneNumber, dateOfBirth, age,
			nationalIdentityCardNumber, nationalIdentificationNumber, passportNumber,
			company, companyEmail, jobTitle, nationality);
	}

	@Override
	public void generateSex() {
		if (sex != null) {
			return;
		}
		sex = baseProducer.trueOrFalse() ? Person.Sex.MALE : Person.Sex.FEMALE;
	}

	@Override
	public void generateCompany() {
		if (company != null) {
			return;
		}
		company = companyFactory.produceCompany().get();
	}

	@Override
	public void generateFirstName() {
		if (firstName != null) {
			return;
		}
		firstName = dataMaster.getValuesOfType(FIRST_NAME, sex.name(), String.class);
	}

	@Override
	public void generateMiddleName() {
		if (middleName != null) {
			return;
		}
		middleName = baseProducer.trueOrFalse() ? dataMaster.getValuesOfType(FIRST_NAME, sex.name(), String.class) : "";
	}

	@Override
	public void generateLastName() {
		if (lastName != null) {
			return;
		}
		lastName = dataMaster.getValuesOfType(LAST_NAME, sex.name(), String.class);
	}

	@Override
	public void generateEmail() {
		if (email != null) {
			return;
		}
		EmailProvider emailProvider = new EmailProvider(dataMaster, baseProducer, firstName, lastName);
		email = emailProvider.get();
	}

	@Override
	public void generateUsername() {
		if (username != null) {
			return;
		}
		if (baseProducer.trueOrFalse()) {
			username = lowerCase(latinize(firstName.substring(0, 1) + lastName));
		} else {
			username = lowerCase(latinize(firstName + lastName.substring(0, 1)));
		}
	}

	@Override
	public void generateTelephoneNumber() {
		if (telephoneNumber != null) {
			return;
		}
		if (telephoneNumberFormat == null) {
			telephoneNumberFormat = dataMaster.getRandomValue(TELEPHONE_NUMBER_FORMATS);
		}
		telephoneNumber = baseProducer.numerify(telephoneNumberFormat);
	}

	@Override
	public void generateMobileTelephoneNumber() {
		if (mobileTelephoneNumber != null) {
			return;
		}
		if (mobileTelephoneNumberFormat == null) {
			mobileTelephoneNumberFormat = dataMaster.getRandomValue(TELEPHONE_NUMBER_FORMATS);
		}
		mobileTelephoneNumber = baseProducer.numerify(mobileTelephoneNumberFormat);
	}

	@Override
	public void generateAge() {
		if (dateOfBirth != null) {
			age = (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDateTime.now());
		} else {
			if (age != null) {
				return;
			}
			age = baseProducer.randomBetween(MIN_AGE, MAX_AGE);
		}
	}

	@Override
	public void generateDateOfBirth() {
		if (dateOfBirth != null) {
			return;
		}
		LocalDate maxDate = timeProvider.getCurrentDate().minusYears(age);
		LocalDate minDate = maxDate.minusYears(1).plusDays(1);
		dateOfBirth = dateProducer.randomDateBetweenTwoDates(minDate, maxDate);
	}

	@Override
	public void generateCompanyEmail() {
		if (companyEmail != null) {
			return;
		}
		CompanyEmailProvider companyEmailProvider = new CompanyEmailProvider(firstName, lastName, company);
		companyEmail = companyEmailProvider.get();
	}

	@Override
	public void generatePassword() {
		if (password != null) {
			return;
		}
		StringBuilder passwordPattern = new StringBuilder();
		for (int i = baseProducer.randomBetween(6, 14); i > 0; i--) {
			passwordPattern.append(baseProducer.randomElement("?", "#"));
		}
		password = baseProducer.bothify(passwordPattern.toString());
	}

	@Override
	public void generateNationalIdentityCardNumber() {
		if (nationalIdentityCardNumber != null) {
			return;
		}
		nationalIdentityCardNumber = nationalIdentityCardNumberProvider.get();
	}

	@Override
	public void generateNationalIdentificationNumber() {
		if (nationalIdentificationNumber != null) {
			return;
		}
		nationalIdentificationNumber = nationalIdentificationNumberFactory.produceNationalIdentificationNumberProvider(
			NationalIdentificationNumberProperties.dateOfBirth(dateOfBirth),
			NationalIdentificationNumberProperties.sex(sex)).get().getValue();
	}

	@Override
	public void generateAddress() {
		if (address != null) {
			return;
		}
		address = addressProvider.get();
	}

	@Override
	public void generatePassportNumber() {
		if (passportNumber != null) {
			return;
		}
		passportNumber = passportNumberProvider.get();
	}

	@Override
	public void generateJobTitle() {
		if (jobTitle != null) {
			return;
		}
		jobTitle = dataMaster.getRandomValue(JOB_TITLE);
	}

	private void generateNationality() {
		List<Country> countries = Country.findCountryForLanguage(dataMaster.getLanguage());
		nationality = !countries.isEmpty() ? baseProducer.randomElement(countries) : Country.UnitedKingdom;
	}

	@Override
	public void setTelephoneNumberFormat(String telephoneFormat) {
		telephoneNumberFormat = telephoneFormat;
	}

	@Override
	public void setMobileTelephoneNumberFormat(String mobileTelephoneNumberFormat) {
		this.mobileTelephoneNumberFormat = mobileTelephoneNumberFormat;
	}

	@Override
	public void setSex(Person.Sex sex) {
		this.sex = sex;
	}

	@Override
	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Override
	public void setMobileTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Override
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	@Override
	public void setNationalIdentityCardNumber(String nationalIdentityCardNumber) {
		this.nationalIdentityCardNumber = nationalIdentityCardNumber;
	}

	@Override
	public void setNationalIdentificationNumber(String nationalIdentificationNumber) {
		this.nationalIdentificationNumber = nationalIdentificationNumber;
	}

	@Override
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	@Override
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
}


