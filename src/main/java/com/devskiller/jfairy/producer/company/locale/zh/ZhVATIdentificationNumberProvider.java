package com.devskiller.jfairy.producer.company.locale.zh;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.RandomGenerator;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.util.ZhFairyUtil;

/**
 * com.devskiller.jfairy.producer.company.locale.zh.ZhVATIdentificationNumberProvider
 * <p>
 * 6 digit for area location, 9 char (0-9A-Z) for organization code (2 char for business type, 2 char for industry type, 5 char for order number), total 15 digit
 *
 * @author lhfcws
 * @since 2017/3/1
 */
public class ZhVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

	private static final BaseProducer BASE_PRODUCER = new BaseProducer(new RandomGenerator());

	@Override
	public String get() {
		StringBuilder vatBuilder = new StringBuilder();
		vatBuilder.append(BASE_PRODUCER.randomElement(ZhFairyUtil.PROV_LIST));
		vatBuilder.append(ZhFairyUtil.getRandomNumStr(BASE_PRODUCER, ZhFairyUtil.CITY_MAX, 2));
		vatBuilder.append(ZhFairyUtil.getRandomNumStr(BASE_PRODUCER, ZhFairyUtil.DISTRICT_MAX, 2));
		vatBuilder.append(getChars(9));
		return vatBuilder.toString();
	}

	private char getChar() {
		int rndNum = BASE_PRODUCER.randomBetween(0, 35);
		if (rndNum < 10) {
			return (char) (49 + rndNum);
		} else {
			return (char) (65 + rndNum - 10);
		}
	}

	private String getChars(int paddingSize) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < paddingSize; i++) {
			sb.append(getChar());
		}
		return sb.toString();
	}
}
