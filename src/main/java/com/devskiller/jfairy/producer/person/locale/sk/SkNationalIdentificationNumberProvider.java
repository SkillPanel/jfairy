package com.devskiller.jfairy.producer.person.locale.sk;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumber;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.Person;

import static java.lang.Integer.valueOf;
import static java.lang.String.format;

/**
 * Slovak National Identification Number (known as Rodne cislo)
 * <p>
 * Based on the same algorithm as Polish PESEL
 * More info: http://en.wikipedia.org/wiki/PESEL
 */
public class SkNationalIdentificationNumberProvider implements NationalIdentificationNumberProvider {

	private static final int NATIONAL_IDENTIFICATION_NUMBER_LENGTH = 11;
	private static final int VALIDITY_IN_YEARS = 10;

	private static final int[] PERIOD_WEIGHTS = {80, 0, 20, 40, 60};
	private static final int PERIOD_FACTOR = 100;
	private static final int BEGIN_YEAR = 1800;

	private static final int[] WEIGHTS = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
	private static final int MAX_SERIAL_NUMBER = 999;
	private static final int TEN = 10;

	private static final int[] SEX_FIELDS = {0, 2, 4, 6, 8};

	private final BaseProducer baseProducer;
	private final DateProducer dateProducer;
	private LocalDate issueDate;
	private Person.Sex sex;

	public SkNationalIdentificationNumberProvider(DateProducer dateProducer, BaseProducer baseProducer,
	                                              NationalIdentificationNumberProperties.Property... properties) {
		this.dateProducer = dateProducer;
		this.baseProducer = baseProducer;

		with(properties);

	}

	public void with(NationalIdentificationNumberProperties.Property[] properties) {
		for (NationalIdentificationNumberProperties.Property property : properties) {
			property.apply(this);
		}
	}

	@Override
	public NationalIdentificationNumber get() {

		if (issueDate == null) {
			issueDate = dateProducer.randomDateInThePast(VALIDITY_IN_YEARS).toLocalDate();
		}
		if (sex == null) {
			sex = baseProducer.trueOrFalse() ? Person.Sex.MALE : Person.Sex.FEMALE;
		}

		return new NationalIdentificationNumber(generate());
	}

	private String generate() {
		int month = calculateMonth(issueDate.getMonthValue(), issueDate.getYear());
		int day = issueDate.getDayOfMonth();
		int serialNumber = baseProducer.randomInt(MAX_SERIAL_NUMBER);
		int sexCode = calculateSexCode(sex);

		String nationalIdentificationNumber = format("%s%02d%02d%03d%d",
			DateTimeFormatter.ofPattern("uu").format(issueDate), month, day, serialNumber, sexCode);

		return nationalIdentificationNumber + calculateChecksum(nationalIdentificationNumber);
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public void setSex(Person.Sex sex) {
		this.sex = sex;
	}

	/**
	 * @param nationalIdentificationNumber National Identification Number
	 * @return nationalIdentificationNumber validity
	 */
	public static boolean isValid(String nationalIdentificationNumber) {
		int size = nationalIdentificationNumber.length();
		if (size != NATIONAL_IDENTIFICATION_NUMBER_LENGTH) {
			return false;
		}

		int checksum = valueOf(nationalIdentificationNumber.substring(size - 1));
		int checkDigit = calculateChecksum(nationalIdentificationNumber);

		return checkDigit == checksum;

	}

	private int calculateMonth(int month, int year) {
		return month + PERIOD_WEIGHTS[(year - BEGIN_YEAR) / PERIOD_FACTOR];
	}

	private int calculateSexCode(Person.Sex sex) {
		return SEX_FIELDS[baseProducer.randomInt(SEX_FIELDS.length - 1)] + (sex == Person.Sex.MALE ? 1 : 0);
	}

	private static int calculateChecksum(String nationalIdentificationNumber) {
		int sum = 0;
		int i = 0;
		for (int weight : WEIGHTS) {
			int digit = Character.digit(nationalIdentificationNumber.charAt(i++), 10);
			sum += digit * weight;
		}

		int checksum = (sum % TEN);

		if (0 == checksum) {
			return 0;
		}

		return TEN - checksum;
	}
}
