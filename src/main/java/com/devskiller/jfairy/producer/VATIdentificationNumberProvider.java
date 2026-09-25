/*
 * Copyright (c) 2013 Codearte and authors
 */
package com.devskiller.jfairy.producer;

import java.util.function.Supplier;

/**
 * VAT identification number (VATIN)
 *
 * @author mariuszs
 * @since 02.11.13.
 */
public interface VATIdentificationNumberProvider extends Supplier<String> {

    @Override
    String get();

    /**
     * Generates a number in the valid format but with a wrong check digit, for negative testing.
     *
     * @return a VAT identification number that fails checksum validation
     * @throws UnsupportedOperationException if the locale has no checksum-based number
     */
    default String getInvalid() {
        throw new UnsupportedOperationException(
            "Invalid VAT identification numbers are not supported by " + getClass().getSimpleName());
    }
}
