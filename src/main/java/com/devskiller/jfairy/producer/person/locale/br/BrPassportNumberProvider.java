package com.devskiller.jfairy.producer.person.locale.br;

import com.devskiller.jfairy.producer.person.PassportNumberProvider;

public class BrPassportNumberProvider implements PassportNumberProvider {

	@Override
	public String get() {
		return "BR" + String.format("%07d", (int) (Math.random() * 10000000));
	}
}
