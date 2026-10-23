package com.devskiller.jfairy.producer.payment;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.function.Supplier;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.util.CheckDigits;

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
        String number = baseProducer.numerify(pattern);
        return number + CheckDigits.luhnDigit(number);
    }
}
