package com.devskiller.jfairy.producer.person.locale.zh;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;

/**
 * com.devskiller.jfairy.producer.person.locale.zh.ZhPassportNumberProvider
 *
 * @author lhfcws
 * @since 2017/3/2
 */
public class ZhPassportNumberProvider implements PassportNumberProvider {

	private final BaseProducer baseProducer;

	public ZhPassportNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return baseProducer.randomAlphanumeric(9);
	}
}
