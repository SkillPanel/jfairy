package com.devskiller.jfairy.producer.person.locale;

import com.devskiller.jfairy.producer.person.NationalIdentificationNumber;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.Person;

import java.time.LocalDate;

public class NoNationalIdentificationNumberFactory implements NationalIdentificationNumberFactory {

    @Override
    public NoNationalIdentificationNumberProvider produceNationalIdentificationNumberProvider(NationalIdentificationNumberProperties.Property... properties) {
        return new NoNationalIdentificationNumberProvider();
    }

    public static class NoNationalIdentificationNumberProvider implements NationalIdentificationNumberProvider {

        @Override
        public NationalIdentificationNumber get() {
            return new NationalIdentificationNumber("");
        }

        @Override
        public void setIssueDate(LocalDate dateOfBirth) {
            // nothing to configure, the number is always empty
        }

        @Override
        public void setSex(Person.Sex sex) {
            // nothing to configure, the number is always empty
        }
    }
}
