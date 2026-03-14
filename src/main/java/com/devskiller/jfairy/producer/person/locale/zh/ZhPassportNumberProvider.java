package com.devskiller.jfairy.producer.person.locale.zh;

import com.devskiller.jfairy.producer.person.PassportNumberProvider;

import static com.devskiller.jfairy.producer.util.RandomUtils.randomAlphanumeric;

/**
 * com.devskiller.jfairy.producer.person.locale.zh.ZhPassportNumberProvider
 *
 * @author lhfcws
 * @since 2017/3/2
 */
public class ZhPassportNumberProvider implements PassportNumberProvider {

	@Override
	public String get() {
		return randomAlphanumeric(9);
	}
}
