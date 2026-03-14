package com.devskiller.jfairy.producer.person;
import java.util.function.Supplier;



public interface NationalIdentityCardNumberProvider extends Supplier<String> {

	String get();
}
