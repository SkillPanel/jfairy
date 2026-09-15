package com.devskiller.jfairy.producer.net;

import com.devskiller.jfairy.producer.BaseProducer;

public class IPNumberProducer {

    private static final int MAX = 0xFF;

    private final BaseProducer baseProducer;

    public IPNumberProducer(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
    }

    public String generate() {
        return String.format("%s.%s.%s.%s", ipNumberPart(), ipNumberPart(), ipNumberPart(), ipNumberPart());
    }

    private int ipNumberPart() {
        return baseProducer.randomInt(MAX);
    }

}
