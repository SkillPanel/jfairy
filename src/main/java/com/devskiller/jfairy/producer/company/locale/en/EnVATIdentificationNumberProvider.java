package com.devskiller.jfairy.producer.company.locale.en;

import java.util.Set;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;

import static java.lang.String.valueOf;
import static java.lang.System.arraycopy;

import static com.devskiller.jfairy.producer.util.StringUtils.leftPad;

/**
 * American VAT Identification Number (known as Employer Identification Number or EIN in the United States)
 * <p>
 * https://en.wikipedia.org/wiki/Employer_Identification_Number
 *
 * @author Olga Maciaszek-Sharma
 * @since 21.03.15
 */
public class EnVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

	private static final int EIN_LENGTH = 10;
	private static final int HYPHEN_INDEX = 2;
	private static final int SERIAL_NUMBER_LENGTH = 7;
	private static final int SERIAL_NUMBER_INDEX = 3;
	private static final int AREA_NUMBER_LENGTH = 2;

	private BaseProducer baseProducer;
	private static final Set<Integer> EXCLUDED_NUMBERS = Set.of(7, 8, 9, 17, 18, 19, 28, 29, 41, 47, 49, 69, 70, 79, 89, 96, 97);


	public EnVATIdentificationNumberProvider(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	@Override
	public String get() {
		char[] ein = new char[EIN_LENGTH];

		fillHyphen(ein);

		fillAreaNumber(ein);

		fillSerialNumber(ein);

		return valueOf(ein);
	}

	private void fillSerialNumber(char[] ein) {
		String number = valueOf(baseProducer.randomBetween(1, 9999));
		char[] digits = leftPad(number, SERIAL_NUMBER_LENGTH, "0").toCharArray();
		arraycopy(digits, 0, ein, SERIAL_NUMBER_INDEX, digits.length);
	}


	private void fillAreaNumber(char[] ein) {
		Integer number;
		do {
			number = baseProducer.randomBetween(0, 99);
		} while (EXCLUDED_NUMBERS.contains(number));
		char[] digits = leftPad(number.toString(), AREA_NUMBER_LENGTH, "0").toCharArray();
		arraycopy(digits, 0, ein, 0, digits.length);

	}

	private void fillHyphen(char[] ein) {
		ein[HYPHEN_INDEX] = '-';
	}
}
