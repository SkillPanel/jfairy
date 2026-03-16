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
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	private final BaseProducer baseProducer;
	private final DateProducer dateProducer;

	public ZhNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
		this.dateProducer = new DateProducer(baseProducer, new TimeProvider());
	}

	@Override
	public String get() {
		StringBuilder idBuilder = new StringBuilder();
		idBuilder.append(baseProducer.randomElement(ZhFairyUtil.PROV_LIST));
		idBuilder.append(ZhFairyUtil.getRandomNumStr(baseProducer, ZhFairyUtil.CITY_MAX, 2));
		idBuilder.append(ZhFairyUtil.getRandomNumStr(baseProducer, ZhFairyUtil.DISTRICT_MAX, 2));
		idBuilder.append(getBirthDate());
		idBuilder.append(ZhFairyUtil.getRandomNumStr(baseProducer, ORDER_MAX, 4));
		return idBuilder.toString();
	}

	private String getBirthDate() {
		LocalDateTime birthDate = this.dateProducer.randomDateInThePast(50);
		return formatter.format(birthDate);
	}
}
