package com.devskiller.jfairy.producer.payment;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.function.Supplier;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;

public class CreditCardProvider implements Supplier<CreditCard> {

    private static final Period DEFAULT_VALIDITY = Period.ofMonths(36);
    private static final String DATA_KEY         = "creditCardPrefixes";

    private final DataMaster   dataMaster;
    private final BaseProducer baseProducer;
    private final DateProducer dateProducer;

    public CreditCardProvider(DataMaster dataMaster, BaseProducer baseProducer, DateProducer dateProducer) {
        this.dataMaster = dataMaster;
        this.baseProducer = baseProducer;
        this.dateProducer = dateProducer;
    }

    /**
     * Generates a credit card of a random vendor.
     *
     * @return a credit card with a valid number
     */
    @Override
    public CreditCard get() {
        return get(baseProducer.randomElement(CreditCardVendor.class));
    }

    /**
     * Generates a credit card of the given vendor.
     *
     * @param vendor the card vendor
     * @return a credit card with a valid number
     * @since 0.10.0
     */
    public CreditCard get(CreditCardVendor vendor) {
        String number = generateNumber(vendor);
        String cvv = baseProducer.numerify("#".repeat(vendor.getCvvLength()));
        LocalDateTime expiryDate = dateProducer.randomDateBetweenNowAndFuturePeriod(DEFAULT_VALIDITY);
        return new CreditCard(vendor.getDisplayName(), number, cvv, expiryDate);
    }

    /**
     * Generates a card number from a random prefix of the vendor, random digits and a Luhn check digit.
     *
     * @param vendor the card vendor
     * @return the complete card number
     */
    private String generateNumber(CreditCardVendor vendor) {
        String prefix = dataMaster.getValuesOfType(DATA_KEY, vendor.getDataKey(), String.class);
        String pattern = prefix + "#".repeat(Math.max(0, vendor.getNumberLength() - 1 - prefix.length()));
        return completeNumber(baseProducer.numerify(pattern));
    }

    /**
     * Completes a credit card number by appending its Luhn check digit.
     * <p>
     * Starting with the rightmost digit, every second digit is doubled, and 9 is subtracted from results above 9.
     * The check digit brings the sum of all digits to a multiple of 10.
     *
     * @param creditCardNumber the card number without its check digit
     * @return the full credit card number including the check digit
     * @throws IllegalArgumentException if the input contains non-digit characters
     */
    private static String completeNumber(String creditCardNumber) {
        int sum = 0;
        boolean doubled = true;
        for (int i = creditCardNumber.length() - 1; i >= 0; i--) {
            int n = Character.digit(creditCardNumber.charAt(i), 10);
            if (n == -1) {
                throw new IllegalArgumentException("Invalid character in credit card number");
            }
            if (doubled) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            doubled = !doubled;
        }
        return creditCardNumber + (10 - sum % 10) % 10;
    }

}
