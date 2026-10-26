package com.devskiller.jfairy.producer.company.locale.it;

import java.util.regex.Pattern;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.util.CheckDigits;

/**
 * Italian VAT identification number (Partita IVA).
 * <p>
 * <a href="https://en.wikipedia.org/wiki/VAT_identification_number">VAT identification number</a>
 * <p>
 * E.g. 12345678903: a seven digit company number, the three digit code of the provincial tax office
 * and a Luhn check digit.
 *
 * @since 0.10.0
 */
public class ItVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

    private static final Pattern FORMAT             = Pattern.compile("^\\d{11}$");
    private static final int     MAX_COMPANY_NUMBER = 9_999_999;
    private static final int     MAX_OFFICE_CODE    = 100;

    private final BaseProducer baseProducer;

    public ItVATIdentificationNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    /**
     * Generates a VAT identification number with a valid check digit.
     *
     * @return an eleven digit VAT identification number
     */
    @Override
    public String get() {
        String number = String.format("%07d%03d",
            baseProducer.randomBetween(1, MAX_COMPANY_NUMBER),
            baseProducer.randomBetween(1, MAX_OFFICE_CODE));
        return number + CheckDigits.luhnDigit(number);
    }

    /**
     * Generates a VAT identification number with a wrong check digit.
     *
     * @return a VAT identification number that fails checksum validation
     */
    @Override
    public String getInvalid() {
        return CheckDigits.replaceLastDigit(get(), baseProducer);
    }

    /**
     * Checks the format and the check digit of a VAT identification number.
     *
     * @param vatIdentificationNumber the number to check
     * @return {@code true} if the number has eleven digits and its check digit matches
     */
    public static boolean isValid(String vatIdentificationNumber) {
        if (!FORMAT.matcher(vatIdentificationNumber).matches()) {
            return false;
        }
        int last = vatIdentificationNumber.length() - 1;
        return CheckDigits.luhnDigit(vatIdentificationNumber.substring(0, last))
            == Character.digit(vatIdentificationNumber.charAt(last), 10);
    }
}
