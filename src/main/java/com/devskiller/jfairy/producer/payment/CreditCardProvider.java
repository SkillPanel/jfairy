package com.devskiller.jfairy.producer.payment;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.function.Supplier;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;

public class CreditCardProvider implements Supplier<CreditCard> {

    private static final Period DEFAULT_VALIDITY = Period.ofMonths(36);
    private static final String DATA_KEY = "creditCardPrefixes";
    private static final String CARD_VENDOR = "Visa";

    private final DataMaster dataMaster;
    private final BaseProducer baseProducer;
    private final DateProducer dateProducer;

    public CreditCardProvider(DataMaster dataMaster, BaseProducer baseProducer, DateProducer dateProducer) {
        this.dataMaster = dataMaster;
        this.baseProducer = baseProducer;
        this.dateProducer = dateProducer;
    }

    @Override
    public CreditCard get() {
        String randomNumber = generateNumber();

        LocalDateTime expiryDate = dateProducer.randomDateBetweenNowAndFuturePeriod(DEFAULT_VALIDITY);
        return new CreditCard(CARD_VENDOR, randomNumber, baseProducer.numerify("###"), expiryDate);
    }

    private String generateNumber() {
        Integer prefix = dataMaster.getValuesOfType(DATA_KEY, CARD_VENDOR, Integer.class);
        String stringPrefix = String.valueOf(prefix);
        StringBuilder builder = new StringBuilder(stringPrefix);
        builder.append("#".repeat(Math.max(0, 15 - stringPrefix.length())));
        return completeNumber(baseProducer.numerify(builder.toString()));
    }

    /**
     * Completes a credit card number by calculating and appending a checksum digit.
     * <p>
     * Uses a variation of the Luhn algorithm where every second digit is doubled.
     * If doubling results in a number greater than 9, the digits are summed.
     *
     * @param creditCardNumber the partial credit card number to complete
     * @return the full credit card number including the checksum digit
     * @throws IllegalArgumentException if the input contains non-digit characters
     */
    private String completeNumber(String creditCardNumber) {
        int sum = 0;
        for (int i = 0; i < creditCardNumber.length(); i++) {
            int n = Character.digit(creditCardNumber.charAt(i), 10);

            if (n == -1) {
                throw new IllegalArgumentException("Invalid character in credit card number");
            }

            if (i % 2 == 0) {
                n *= 2;
                if (n > 9) {
                    // For n > 9 (e.g., 10-18), digit sum is equivalent to n - 9
                    n -= 9;
                }
            }
            sum += n;
        }

        int checksum = sum * 9 % 10;
        return creditCardNumber + checksum;
    }

}
