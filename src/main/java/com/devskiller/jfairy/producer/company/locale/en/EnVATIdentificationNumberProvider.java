package com.devskiller.jfairy.producer.company.locale.en;

import java.util.Set;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.util.StringUtils;

/**
 * American VAT Identification Number (known as Employer Identification Number or EIN in the United States)
 * <p>
 * <a href="https://en.wikipedia.org/wiki/Employer_Identification_Number">Employer Identification Number</a>
 *
 * @author Olga Maciaszek-Sharma
 * @since 21.03.15
 */
public class EnVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

    private static final int EIN_LENGTH = 10;
    private static final int HYPHEN_INDEX = 2;
    private static final int SERIAL_NUMBER_LENGTH = 7;
    private static final int SERIAL_NUMBER_INDEX = 3;
    private static final int AREA_NUMBER_LENGTH = 2;

    private final BaseProducer baseProducer;
    private static final Set<Integer> EXCLUDED_NUMBERS = Set.of(7, 8, 9, 17, 18, 19, 28, 29, 41, 47, 49, 69, 70, 79, 89, 96, 97);

    public EnVATIdentificationNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    @Override
    public String get() {
        char[] ein = new char[EIN_LENGTH];

        fillHyphen(ein);

        fillAreaNumber(ein);

        fillSerialNumber(ein);

        return String.valueOf(ein);
    }

    private void fillSerialNumber(char[] ein) {
        String number = String.valueOf(baseProducer.randomBetween(1, 9999));
        char[] digits = StringUtils.leftPad(number, SERIAL_NUMBER_LENGTH, "0").toCharArray();
        System.arraycopy(digits, 0, ein, SERIAL_NUMBER_INDEX, digits.length);
    }

    private void fillAreaNumber(char[] ein) {
        int number;
        do {
            number = baseProducer.randomBetween(0, 99);
        } while (EXCLUDED_NUMBERS.contains(number));
        char[] digits = StringUtils.leftPad(Integer.toString(number), AREA_NUMBER_LENGTH, "0").toCharArray();
        System.arraycopy(digits, 0, ein, 0, digits.length);

    }

    private void fillHyphen(char[] ein) {
        ein[HYPHEN_INDEX] = '-';
    }

}
