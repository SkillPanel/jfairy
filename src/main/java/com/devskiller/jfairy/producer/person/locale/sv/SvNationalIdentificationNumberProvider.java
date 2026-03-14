package com.devskiller.jfairy.producer.person.locale.sv;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumber;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.Person;

import static java.lang.String.format;

/**
 * Swedish National Identification Number (known as Personal Identity Number or Personnummer in sweden)
 * <p>
 * https://en.wikipedia.org/wiki/Personal_identity_number_(Sweden)
 * <p>
 * E.g.
 * 870508-5853
 * <p>
 * The left part of the hyphen consists of the date of birth and the right part consist of
 * three random numbers + one check digit.
 */
public class SvNationalIdentificationNumberProvider implements NationalIdentificationNumberProvider {

	private static final int NATIONAL_IDENTIFICATION_NUMBER_LENGTH = 11;
	private static final int VALIDITY_IN_YEARS = 120;


	private static final int[] WEIGHTS = {2, 1, 2, 1, 2, 1, 2, 1, 2};
	private static final int MAX_SERIAL_NUMBER = 99;
	private static final int TEN = 10;

	private static final int[] SEX_FIELDS = {0, 2, 4, 6, 8};

	private final BaseProducer baseProducer;
	private final DateProducer dateProducer;
	private LocalDate issueDate;
	private Person.Sex sex;


	public SvNationalIdentificationNumberProvider(DateProducer dateProducer, BaseProducer baseProducer,
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
		int serialNumber = baseProducer.randomInt(MAX_SERIAL_NUMBER);
		int sexCode = calculateSexCode(sex);

		String nationalIdentificationNumber = format("%s%s%s-%02d%d",
			DateTimeFormatter.ofPattern("uu").format(issueDate),
			DateTimeFormatter.ofPattern("MM").format(issueDate),
			DateTimeFormatter.ofPattern("dd").format(issueDate), serialNumber, sexCode);

		return nationalIdentificationNumber + calculateChecksum(nationalIdentificationNumber);
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public void setSex(Person.Sex sex) {
		this.sex = sex;
	}

	/**
	 * @param nationalIdentificationNumber Personal Identity Number
	 * @return nationalIdentificationNumber validity
	 */
	public static boolean isValid(String nationalIdentificationNumber) {
		int size = nationalIdentificationNumber.length();
		if (size != NATIONAL_IDENTIFICATION_NUMBER_LENGTH) {
			return false;
		}

		int checksum = Integer.valueOf(nationalIdentificationNumber.substring(size - 1));
		int checkDigit = calculateChecksum(nationalIdentificationNumber);

		return checkDigit == checksum;

	}

	private int calculateSexCode(Person.Sex sex) {
		return SEX_FIELDS[baseProducer.randomInt(SEX_FIELDS.length - 1)] + (sex == Person.Sex.MALE ? 1 : 0);
	}

	public static int calculateChecksum(String nationalIdentificationNumber) {
		String nationalIdentificationNumberWithoutHyphen = nationalIdentificationNumber.replace("-", "");
		int sum = 0;
		int i = 0;
		for (int weight : WEIGHTS) {
			int digit = Character.digit(nationalIdentificationNumberWithoutHyphen.charAt(i++), 10);
			int product = digit * weight;
			sum += (product / 10) + (product % 10);
		}

		if (sum % 10 == 0) {
			return 0;
		}

		return TEN - (sum % 10);
	}

}

