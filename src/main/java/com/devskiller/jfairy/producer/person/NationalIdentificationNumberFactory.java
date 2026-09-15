package com.devskiller.jfairy.producer.person;

public interface NationalIdentificationNumberFactory {

    NationalIdentificationNumberProvider produceNationalIdentificationNumberProvider(NationalIdentificationNumberProperties.Property... properties);

}
