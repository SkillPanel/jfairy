package com.devskiller.jfairy.producer.payment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

/**
 * Representation of a faked credit card with its essential security and expiry data.
 * <p>
 * This class holds information about the card vendor, number, CVV code,
 * and the expiration date. It is designed as an immutable object.
 *
 * @author jkubrynski@gmail.com
 * @since 2013-11-17
 */
public class CreditCard {

    private final String cardVendor;
    private final String cardNumber;
    private final String cvv;
    private final LocalDateTime expiryDate;

    public CreditCard(String cardVendor, String cardNumber, String cvv, LocalDateTime expiryDate) {
        this.cardVendor = cardVendor;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    public String getVendor() {
        return cardVendor;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public String getExpiryDateAsString() {
        return String.format("%02d/%s", expiryDate.getMonthValue(), DateTimeFormatter.ofPattern("uu").format(expiryDate));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
            .add("cardVendor=" + cardVendor)
            .add("cardNumber=" + cardNumber)
            .add("cvv=" + cvv)
            .add("expiryDate=" + getExpiryDateAsString())
            .toString();
    }
}
