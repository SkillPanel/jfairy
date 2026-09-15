package com.devskiller.jfairy.producer.person.locale.ja;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;

/**
 * Japanese "My Number" (個人番号), a 12-digit individual number assigned to every resident.
 */
public class JaNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

    private static final int DIGIT_COUNT = 12;

    private final BaseProducer baseProducer;

    public JaNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    @Override
    public String get() {
        StringBuilder sb = new StringBuilder(DIGIT_COUNT);
        for (int i = 0; i < DIGIT_COUNT; i++) {
            sb.append(baseProducer.randomInt(9));
        }
        return sb.toString();
    }
}
