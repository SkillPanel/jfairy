package com.devskiller.jfairy.producer.payment;

import java.util.List;
import java.util.stream.Collectors;

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
        // Without a seed RandomIban falls back to ThreadLocalRandom, which cannot be seeded; derive
        // a seed from our generator so the result stays deterministic under withRandomSeed(...).
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
        // Pick uniformly from [MIN_CHECK_DIGITS, MAX_CHECK_DIGITS] \ {validCheckDigits} by drawing from
        // the 96 remaining values and shifting past the valid one if we land on or above it.
        int wrongCheckDigits = baseProducer.randomBetween(MIN_CHECK_DIGITS, MAX_CHECK_DIGITS - 1);
        if (wrongCheckDigits >= validCheckDigits) {
            wrongCheckDigits++;
        }
        String checkDigits = String.format("%02d", wrongCheckDigits);

        return new IBAN(valid.getAccountNumber(),
                        checkDigits,
                        valid.getBankCode(),
                        valid.getBban(),
                        valid.getCountry(),
                        valid.getNationalCheckDigit(),
                        valid.getCountry() + checkDigits + valid.getBban());
    }

    @Override
    public void fillCountryCode() {
        if (countryCode == null) {
            // Country.findCountryForLanguage() returns every country speaking the language, most of
            // which have no IBAN (e.g. US, AU for English); restrict the pick to ones that do so a
            // supported language doesn't randomly yield a null IBAN.
            List<Country> countries = Country.findCountryForLanguage(dataMaster.getLanguage()).stream()
                .filter(country -> IbanRegistry.getByCode(country.getCode()) != null)
                .collect(Collectors.toList());
            if (!countries.isEmpty()) {
                countryCode = baseProducer.randomElement(countries).getCode();
            }
        }
    }

    @Override
    public void setCountry(String country) {
        this.countryCode = country;
    }

}
