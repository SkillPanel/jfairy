package com.devskiller.jfairy.producer.person.locale.pl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumber;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.util.CheckDigits;

/**
 * Polish National Identification Number (known as PESEL - Powszechny Elektroniczny System Ewidencji Ludności)
 * <p>
 * Universal Electronic System for Registration of the Population in Poland.
 * The number consists of 11 digits and includes information about birth date and sex.
 * More info: <a href="http://en.wikipedia.org/wiki/PESEL">PESEL</a>
 */
public class PlNationalIdentificationNumberProvider implements NationalIdentificationNumberProvider {

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

    /**
     * Initializes a new provider for Polish PESEL numbers
     * <p>
     * @param dateProducer provider for generating dates
     * @param baseProducer provider for random numbers and booleans
     * @param properties optional properties to constrain the generated number
     */
    public PlNationalIdentificationNumberProvider(DateProducer dateProducer, BaseProducer baseProducer,
                                                   NationalIdentificationNumberProperties.Property... properties) {
        this.dateProducer = dateProducer;
        this.baseProducer = baseProducer;

        with(properties);
    }

    /**
     * Applies specific properties to the provider
     * <p>
     * @param properties array of properties like sex or birth date
     */
    private void with(NationalIdentificationNumberProperties.Property[] properties) {
        for (NationalIdentificationNumberProperties.Property property : properties) {
            property.apply(this);
        }
    }

    /**
     * Generates a random Polish National Identification Number
     * <p>
     * @return a new NationalIdentificationNumber instance containing the PESEL
     */
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

    /**
     * Generates a PESEL with the requested birth date and sex but a wrong check digit
     * <p>
     * @return a new NationalIdentificationNumber instance that fails checksum validation
     */
    @Override
    public NationalIdentificationNumber getInvalid() {
        return new NationalIdentificationNumber(CheckDigits.replaceLastDigit(get().getValue(), baseProducer));
    }

    private String generate() {
        int month = calculateMonth(issueDate.getMonthValue(), issueDate.getYear());
        int day = issueDate.getDayOfMonth();
        int serialNumber = baseProducer.randomInt(MAX_SERIAL_NUMBER);
        int sexCode = calculateSexCode(sex);

        String nationalIdentificationNumber = String.format("%s%02d%02d%03d%d",
            DateTimeFormatter.ofPattern("uu").format(issueDate), month, day, serialNumber, sexCode);

        return nationalIdentificationNumber + calculateChecksum(nationalIdentificationNumber);
    }

    @Override
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    @Override
    public void setSex(Person.Sex sex) {
        this.sex = sex;
    }

    /**
     * Verifies the validity of a given PESEL number based on its length and checksum
     * <p>
     * @param nationalIdentificationNumber the PESEL string to verify
     * @return true if the number is valid, false otherwise
     */
    public static boolean isValid(String nationalIdentificationNumber) {
        int size = nationalIdentificationNumber.length();
        if (size != NATIONAL_IDENTIFICATION_NUMBER_LENGTH) {
            return false;
        }

        int checksum = Integer.parseInt(nationalIdentificationNumber.substring(size - 1));
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

        int checksum = sum % TEN;

        if (0 == checksum) {
            return 0;
        }

        return TEN - checksum;
    }
}
