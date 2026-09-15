package com.devskiller.jfairy.producer.payment;

public interface IBANFactory {

    IBANProvider produceIBANProvider(IBANProperties.Property... properties);

}
