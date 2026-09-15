package com.devskiller.jfairy.producer.person.locale.ja;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.TimeProvider;
import com.devskiller.jfairy.producer.company.CompanyFactory;
import com.devskiller.jfairy.producer.person.AddressProvider;
import com.devskiller.jfairy.producer.person.DefaultPersonProvider;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.NationalIdentityCardNumberProvider;
import com.devskiller.jfairy.producer.person.PassportNumberProvider;
import com.devskiller.jfairy.producer.person.PersonProperties;

/**
 * Japanese person provider. The default {@code generateEmail()}/{@code generateUsername()}/
 * {@code generateCompanyEmail()} build the local part directly from the (Kanji/Katakana) first and last
 * name and only {@link com.devskiller.jfairy.producer.util.StringUtils#latinize} it, which strips Latin
 * diacritics but leaves CJK characters untouched, producing invalid addresses such as
 * {@code 松尾@softbank.ne.jp}. Since jfairy has no reading (furigana) data to transliterate an arbitrary
 * Kanji name into romaji, this provider instead builds a random, always-ASCII local part.
 */
public class JaPersonProvider extends DefaultPersonProvider {

    private static final int HANDLE_LETTER_COUNT = 6;
    private static final int HANDLE_DIGIT_COUNT = 2;

    public JaPersonProvider(DataMaster dataMaster, DateProducer dateProducer, BaseProducer baseProducer,
                             AddressProvider addressProvider,
                             NationalIdentificationNumberFactory nationalIdentificationNumberFactory,
                             NationalIdentityCardNumberProvider nationalIdentityCardNumberProvider,
                             PassportNumberProvider passportNumberProvider, TimeProvider timeProvider,
                             CompanyFactory companyFactory, PersonProperties.PersonProperty... personProperties) {
        super(dataMaster, dateProducer, baseProducer, addressProvider, nationalIdentificationNumberFactory,
            nationalIdentityCardNumberProvider, passportNumberProvider, timeProvider, companyFactory, personProperties);
    }

    private String randomHandle() {
        return (baseProducer.randomAlphabetic(HANDLE_LETTER_COUNT) + baseProducer.randomNumeric(HANDLE_DIGIT_COUNT)).toLowerCase();
    }

    @Override
    public void generateEmail() {
        if (email != null) {
            return;
        }
        email = randomHandle() + '@' + dataMaster.getRandomValue(PERSONAL_EMAIL);
    }

    @Override
    public void generateUsername() {
        if (username != null) {
            return;
        }
        username = randomHandle();
    }

    @Override
    public void generateCompanyEmail() {
        if (companyEmail != null) {
            return;
        }
        companyEmail = randomHandle() + '@' + company.getDomain();
    }
}
