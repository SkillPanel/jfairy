package com.devskiller.jfairy.producer.person;
import java.util.function.Supplier;


/**
 * @author Olga Maciaszek-Sharma
 * @since 21.02.15
 */
public interface PassportNumberProvider extends Supplier<String> {

	@Override
	String get();

}
