package com.devskiller.jfairy.producer.company.locale.es;

import java.util.Locale;
import java.util.regex.Pattern;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.Country;

/**
 * Spanish VAT Identification Number (known as Número de Identificación Fiscal (for freelancers) or Código de Identificación Fiscal (for companies)  in Spain)
 * <p>
 * <a href="https://en.wikipedia.org/wiki/VAT_identification_number">VAT identification number</a>
 */
public class EsVATIdentificationNumberProvider implements VATIdentificationNumberProvider {

    private static final String REGEX_CIF = "^[A-Z][0-9]{2}[0-9]{5}([KPQSABEH]|[0-9]|[A-Z])$";

    private final BaseProducer baseProducer;

    private final Pattern regexCif;

    private final Locale primaryLocale = Country.Spain.getPrimaryLocale();

    public EsVATIdentificationNumberProvider(BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
        this.regexCif = Pattern.compile(REGEX_CIF);
    }

    @Override
    public String get() {
        return String.format("%s%s%s",
            baseProducer.randomAlphabetic(1).toUpperCase(primaryLocale),
            baseProducer.randomNumeric(7),
            baseProducer.randomAlphanumeric(1).toUpperCase(primaryLocale));
    }

    public boolean isValid(String cif) {
        return this.regexCif.matcher(cif).matches();
    }
}
