package com.devskiller.jfairy.producer.payment;

import java.util.List;
import java.util.Optional;

import de.speedbanking.iban.Iban;
import de.speedbanking.iban.IbanRegistry;
import de.speedbanking.iban.RandomIban;
import org.jspecify.annotations.Nullable;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.Country;

/**
 * ALPHA: Under development
 */
public class DefaultIBANProvider implements IBANProvider {

    // Valid IBAN check digits range from 02 to 98; these 97 values are pairwise distinct modulo 97,
    // so any value in this range other than the valid one fails the mod-97 check.
    private static final int MIN_CHECK_DIGITS = 2;
    private static final int MAX_CHECK_DIGITS = 98;

    protected final DataMaster dataMaster;
    protected final BaseProducer baseProducer;
    protected String countryCode;

    public DefaultIBANProvider(BaseProducer baseProducer,
                               DataMaster dataMaster,
                               IBANProperties.Property... properties) {
        this.dataMaster = dataMaster;
        this.baseProducer = baseProducer;
        for (IBANProperties.Property property : properties) {
            property.apply(this);
        }
    }

    @Override
    public @Nullable IBAN get() {
        fillCountryCode();

        IbanRegistry reg = IbanRegistry.getByCode(countryCode);
        if (reg == null) {
            return null;
        }
        // RandomIban uses its own unseeded Random by default; derive a seed from our generator
        // so the result stays deterministic under Fairy.builder().withRandomSeed(...).
        Iban iban = RandomIban.builder()
                .country(reg)
                .seed(baseProducer.randomBetween(Long.MIN_VALUE, Long.MAX_VALUE))
                .build();

        return new IBAN(iban.getAccountNumber(),
                        iban.getCheckDigits(),
                        iban.getBankCode(),
                        iban.getBban(),
                        iban.getCountryCode(),
                        iban.getNationalCheckDigit(),
                        iban.toString());
    }

    @Override
    public @Nullable IBAN getInvalid() {
        IBAN valid = get();
        if (valid == null) {
            return null;
        }

        int validCheckDigits = Integer.parseInt(valid.getCheckDigit());
        int wrongCheckDigits = baseProducer.randomBetween(MIN_CHECK_DIGITS, MAX_CHECK_DIGITS - 1);
        if (wrongCheckDigits >= validCheckDigits) {
            wrongCheckDigits++;
        }
        String checkDigits = String.format("%02d", wrongCheckDigits);
        String ibanNumber = valid.getIbanNumber();

        return new IBAN(valid.getAccountNumber(),
                        checkDigits,
                        valid.getBankCode(),
                        valid.getBban(),
                        valid.getCountry(),
                        valid.getNationalCheckDigit(),
                        ibanNumber.substring(0, 2) + checkDigits + ibanNumber.substring(4));
    }

    @Override
    public void fillCountryCode() {
        if (countryCode == null) {
            List<Country> countries = Country.findCountryForLanguage(dataMaster.getLanguage());
            Country country = baseProducer.randomElement(countries);

            IbanRegistry r = IbanRegistry.getByCode(country.getCode());
            countryCode = Optional.ofNullable(r).map(IbanRegistry::name).orElse(null);
        }
    }

    @Override
    public void setCountry(String country) {
        this.countryCode = country;
    }

}
