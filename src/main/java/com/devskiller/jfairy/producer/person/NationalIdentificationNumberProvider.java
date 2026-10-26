package com.devskiller.jfairy.producer.person;

import java.time.LocalDate;
import java.util.function.Supplier;

public interface NationalIdentificationNumberProvider extends Supplier<NationalIdentificationNumber> {

    @Override
    NationalIdentificationNumber get();

    /**
     * Generates a number in the valid format but with a wrong check digit, for negative testing.
     *
     * @return a national identification number that fails checksum validation
     * @throws UnsupportedOperationException if invalid numbers are not implemented for this locale
     */
    default NationalIdentificationNumber getInvalid() {
        throw new UnsupportedOperationException(
            "Invalid national identification numbers are not supported by " + getClass().getName());
    }

    void setIssueDate(LocalDate dateOfBirth);

    void setSex(Person.Sex sex);

    /**
     * Sets the holder's first name. Ignored by locales whose number does not depend on the name.
     *
     * @param firstName the holder's first name
     * @since 0.10.0
     */
    default void setFirstName(String firstName) {
        // most numbers do not encode the name
    }

    /**
     * Sets the holder's last name. Ignored by locales whose number does not depend on the name.
     *
     * @param lastName the holder's last name
     * @since 0.10.0
     */
    default void setLastName(String lastName) {
        // most numbers do not encode the name
    }
}
