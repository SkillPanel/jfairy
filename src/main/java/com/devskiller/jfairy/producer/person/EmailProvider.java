package com.devskiller.jfairy.producer.person;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.util.TextUtils;

import static com.devskiller.jfairy.producer.person.PersonProvider.PERSONAL_EMAIL;
import static org.apache.commons.lang3.StringUtils.lowerCase;

public class EmailProvider implements Supplier<String> {

	private final DataMaster dataMaster;
	private final BaseProducer baseProducer;
	private final String firstName;
	private final String lastName;

	public EmailProvider(DataMaster dataMaster, BaseProducer baseProducer,
						 String firstName, String lastName) {
		this.dataMaster = dataMaster;
		this.baseProducer = baseProducer;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String get() {
		String prefix = "";
		switch (baseProducer.randomBetween(1, 3)) {
			case 1:
				prefix = StringUtils.replace(firstName + lastName, " ", "");
				break;
			case 2:
				prefix = StringUtils.replace(firstName + "." + lastName, " ", ".");
				break;
			case 3:
				prefix = StringUtils.replace(lastName, " ", "");
				break;
		}
		String email = lowerCase(prefix + '@' + dataMaster.getRandomValue(PERSONAL_EMAIL));
		return TextUtils.stripSharpS(TextUtils.stripAccents(email));
	}
}
