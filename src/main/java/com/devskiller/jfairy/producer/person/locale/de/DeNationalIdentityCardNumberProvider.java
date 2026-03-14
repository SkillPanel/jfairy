package com.devskiller.jfairy.producer.person.locale.de;


import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;

import static com.devskiller.jfairy.producer.util.RandomUtils.randomNumeric;

/**
 * German Identity Card (known as Personalausweis)
 * <p>
 * https://en.wikipedia.org/wiki/German_identity_card
 *
 * @author Roland Weisleder
 */
public class DeNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

	private static final String[] ID_CARD_TYPE_LETTERS = {"L", "M", "N", "P", "R", "T", "V", "W", "X", "Y"};

	private static final String VALID_NUMBER_PATTERN = "^[LMNPRTVWXY][0-9]{8}$";

	private final BaseProducer baseProducer;


	public DeNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		return baseProducer.randomElement(ID_CARD_TYPE_LETTERS) + randomNumeric(8);
	}

	public boolean isValid(String nationalIdentityCardNumber) {
		return nationalIdentityCardNumber.matches(VALID_NUMBER_PATTERN);
	}

}
