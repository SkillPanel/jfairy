package com.devskiller.jfairy.producer.person;

import java.time.LocalDate;

public final class NationalIdentificationNumberProperties {

    private NationalIdentificationNumberProperties() {
    }

    public static Property dateOfBirth(final LocalDate dateOfBirth) {
        return new Property() {
            @Override
            public void apply(NationalIdentificationNumberProvider nationalIdentificationNumberProvider) {
                nationalIdentificationNumberProvider.setIssueDate(dateOfBirth);
            }
        };
    }

    public static Property sex(final Person.Sex sex) {
        return new Property() {
            @Override
            public void apply(NationalIdentificationNumberProvider nationalIdentificationNumberProvider) {
                nationalIdentificationNumberProvider.setSex(sex);
            }
        };
    }

    /**
     * Sets the first name, for locales whose number is derived from the holder's name.
     *
     * @param firstName the holder's first name
     * @return the property
     * @since 0.10.0
     */
    public static Property firstName(final String firstName) {
        return new Property() {
            @Override
            public void apply(NationalIdentificationNumberProvider nationalIdentificationNumberProvider) {
                nationalIdentificationNumberProvider.setFirstName(firstName);
            }
        };
    }

    /**
     * Sets the last name, for locales whose number is derived from the holder's name.
     *
     * @param lastName the holder's last name
     * @return the property
     * @since 0.10.0
     */
    public static Property lastName(final String lastName) {
        return new Property() {
            @Override
            public void apply(NationalIdentificationNumberProvider nationalIdentificationNumberProvider) {
                nationalIdentificationNumberProvider.setLastName(lastName);
            }
        };
    }

    public abstract static class Property {

        public abstract void apply(NationalIdentificationNumberProvider nationalIdentificationNumberProvider);
    }

}
