package com.devskiller.jfairy.producer.person.locale.br;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

public class BrPassportNumberProvider implements PassportNumberProvider {

    private final BaseProducer baseProducer;

    public BrPassportNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    @Override
    public String get() {
        return "BR" + baseProducer.randomNumeric(7);
    }
}
