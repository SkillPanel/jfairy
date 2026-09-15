package com.devskiller.jfairy.producer.person.locale.en;

import java.util.List;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.util.StringUtils;

/**
 * English National Identity Card Number (known as Social Security Number)
 *
 * @author Olga Maciaszek-Sharma
 * @since 15.03.15
 */
public class EnNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

    private static final int SSN_LENGTH = 11;
    private static final int AREA_NUMBER_LENGTH = 3;
    private static final int GROUP_NUMBER_LENGTH = 2;
    private static final int GROUP_NUMBER_INDEX = 4;
    private static final List<Integer> HYPHEN_INDEXES = List.of(3, 6);
    private static final int SERIAL_NUMBER_LENGTH = 4;
    private static final int SERIAL_NUMBER_INDEX = 7;

    private final BaseProducer baseProducer;

    public EnNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    @Override
    public String get() {
        char[] ssn = new char[SSN_LENGTH];

        fillHyphens(ssn);

        fillAreaNumber(ssn);

        fillGroupNumber(ssn);

        fillSerialNumber(ssn);

        return String.valueOf(ssn);
    }

    private void fillHyphens(char[] ssn) {
        for (Integer index : HYPHEN_INDEXES) {
            ssn[index] = '-';
        }
    }

    private void fillAreaNumber(char[] ssn) {
        String number;
        do {
            number = String.valueOf(baseProducer.randomBetween(1, 899));
        } while ("666".equals(number));
        char[] digits = StringUtils.leftPad(number, AREA_NUMBER_LENGTH, "0").toCharArray();
        System.arraycopy(digits, 0, ssn, 0, digits.length);
    }

    private void fillGroupNumber(char[] ssn) {
        String number = String.valueOf(baseProducer.randomBetween(1, 99));
        char[] digits = StringUtils.leftPad(number, GROUP_NUMBER_LENGTH, "0").toCharArray();
        System.arraycopy(digits, 0, ssn, GROUP_NUMBER_INDEX, digits.length);
    }

    private void fillSerialNumber(char[] ssn) {
        String number = String.valueOf(baseProducer.randomBetween(1, 9999));
        char[] digits = StringUtils.leftPad(number, SERIAL_NUMBER_LENGTH, "0").toCharArray();
        System.arraycopy(digits, 0, ssn, SERIAL_NUMBER_INDEX, digits.length);
    }

}

