package com.devskiller.jfairy.producer.person;

import java.util.function.Supplier;

public interface AddressProvider extends Supplier<Address> {

    @Override
    Address get();
}
