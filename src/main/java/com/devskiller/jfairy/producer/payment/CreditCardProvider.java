package com.devskiller.jfairy.producer.payment;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.function.Supplier;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;

public class CreditCardProvider implements Supplier<CreditCard> {

	private static final Period DEFAULT_VALIDITY = Period.ofMonths(36);
	private static final String DATA_KEY = "creditCardPrefixes";
	private static final String CARD_VENDOR = "Visa";

	private final DataMaster dataMaster;
	private final BaseProducer baseProducer;
	private final DateProducer dateProducer;


	public CreditCardProvider(DataMaster dataMaster, BaseProducer baseProducer, DateProducer dateProducer) {
		this.dataMaster = dataMaster;
		this.baseProducer = baseProducer;
		this.dateProducer = dateProducer;
	}

	@Override
	public CreditCard get() {
		String randomNumber = generateNumber();

		LocalDateTime expiryDate = dateProducer.randomDateBetweenNowAndFuturePeriod(DEFAULT_VALIDITY);
		return new CreditCard(CARD_VENDOR, randomNumber, baseProducer.numerify("###"), expiryDate);
	}

	private String generateNumber() {
		Integer prefix = dataMaster.getValuesOfType(DATA_KEY, CARD_VENDOR, Integer.class);
		String stringPrefix = String.valueOf(prefix);
		StringBuilder builder = new StringBuilder(stringPrefix);
		for (int i = stringPrefix.length(); i < 15; i++) {
			builder.append("#");
		}
		return completeNumber(baseProducer.numerify(builder.toString()));
	}

	private String completeNumber(String creditCardNumber) {
		int sum = 0;
		for (int i = 0; i < creditCardNumber.length(); i++) {
			int n = Character.getNumericValue(creditCardNumber.charAt(i));
			if (i % 2 == 0) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
		}
		return creditCardNumber + sum * 9 % 10;
	}

}
