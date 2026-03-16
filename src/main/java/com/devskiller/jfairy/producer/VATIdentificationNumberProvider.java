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

	String get();
}
