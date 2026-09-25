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
}
