package com.devskiller.jfairy.producer.person.locale.zh;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.TimeProvider;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.util.ZhFairyUtil;

/**
 * Chinese National Identity Card Number, total 18 digits
 *
 * @author Lhfcws
 * @since 27.02.17
 */
public class ZhNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

    /**
     * The last 4 digit is an order number from 0001 to 9999
     */
    private static final int ORDER_MAX = 9999;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final BaseProducer baseProducer;
    private final DateProducer dateProducer;

    public ZhNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
        this.dateProducer = new DateProducer(baseProducer, new TimeProvider());
    }

    @Override
    public String get() {
        return baseProducer.randomElement(ZhFairyUtil.PROV_LIST)
                + ZhFairyUtil.getRandomNumStr(baseProducer, ZhFairyUtil.CITY_MAX, 2)
                + ZhFairyUtil.getRandomNumStr(baseProducer, ZhFairyUtil.DISTRICT_MAX, 2)
                + getBirthDate()
                + ZhFairyUtil.getRandomNumStr(baseProducer, ORDER_MAX, 4);
    }

    private String getBirthDate() {
        LocalDateTime birthDate = this.dateProducer.randomDateInThePast(50);
        return FORMATTER.format(birthDate);
    }
}
