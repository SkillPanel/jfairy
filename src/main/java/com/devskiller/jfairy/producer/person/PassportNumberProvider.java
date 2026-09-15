package com.devskiller.jfairy.producer.person;

import java.util.function.Supplier;

/**
 * Provider for generating localized or random passport numbers.
 * <p>
 * This functional interface extends {@link Supplier} to provide a standardized
 * way of retrieving passport identification strings for different locales.
 *
 * @author Olga Maciaszek-Sharma
 * @since 21.02.15
 */
public interface PassportNumberProvider extends Supplier<String> {

    @Override
    String get();

}
