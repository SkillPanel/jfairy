package com.devskiller.jfairy.producer.payment;

/**
 * Card vendors a {@link CreditCard} can be generated for.
 * <p>
 * Each vendor defines the length of the card number and of the security code. The number prefixes are data, kept
 * under {@code creditCardPrefixes.<display name>} with spaces replaced by underscores (e.g.
 * {@code creditCardPrefixes.American_Express}) so they can be overridden like any other data value.
 *
 * @since 0.10.0
 */
public enum CreditCardVendor {

    VISA("Visa", 16, 3),
    MASTERCARD("Mastercard", 16, 3),
    AMERICAN_EXPRESS("American Express", 15, 4),
    DISCOVER("Discover", 16, 3),
    JCB("JCB", 16, 3);

    private final String displayName;
    private final int    numberLength;
    private final int    cvvLength;

    /**
     * Creates a vendor.
     *
     * @param displayName  the name returned by {@link CreditCard#getVendor()}
     * @param numberLength the number of digits of a card number, including the check digit
     * @param cvvLength    the number of digits of the security code
     */
    CreditCardVendor(String displayName, int numberLength, int cvvLength) {
        this.displayName = displayName;
        this.numberLength = numberLength;
        this.cvvLength = cvvLength;
    }

    /**
     * Returns the vendor name as printed on cards, e.g. {@code American Express}.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the number of digits of a card number, including the check digit.
     *
     * @return the card number length
     */
    public int getNumberLength() {
        return numberLength;
    }

    /**
     * Returns the number of digits of the card security code, see {@link CreditCard#getCvv()}.
     * <p>
     * CVV (card verification value) is Visa's name for it and the one most commonly used for all vendors.
     * Mastercard calls it CVC (card validation code), American Express and Discover CID (card identification
     * number), JCB CAV2 (card authentication value).
     *
     * @return the security code length
     */
    public int getCvvLength() {
        return cvvLength;
    }

    /**
     * Returns the key of this vendor's number prefixes below {@code creditCardPrefixes}.
     *
     * @return the display name with spaces replaced by underscores
     */
    String getDataKey() {
        return displayName.replace(' ', '_');
    }
}
