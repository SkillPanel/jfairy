package com.devskiller.jfairy.producer.company;

import java.util.Comparator;
import java.util.Locale;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;

import static com.devskiller.jfairy.producer.util.StringUtils.escapeNonAscii;
import static com.devskiller.jfairy.producer.util.StringUtils.latinize;

public class DefaultCompanyProvider implements CompanyProvider {

    protected String name;
    protected String domain;
    protected String email;
    protected String vatIdentificationNumber;

    protected final BaseProducer baseProducer;
    protected final DataMaster dataMaster;

    protected final VATIdentificationNumberProvider vatIdentificationNumberProvider;

    public DefaultCompanyProvider(BaseProducer baseProducer,
                              DataMaster dataMaster,
                              VATIdentificationNumberProvider vatIdentificationNumberProvider,
                              CompanyProperties.CompanyProperty... companyProperties) {
        this.baseProducer = baseProducer;
        this.dataMaster = dataMaster;
        this.vatIdentificationNumberProvider = vatIdentificationNumberProvider;

        for (CompanyProperties.CompanyProperty companyProperty : companyProperties) {
            companyProperty.apply(this);
        }
    }

    @Override
    public Company get() {

        generateName();
        generateDomain();
        generateEmail();
        generateVatIdentificationNumber();

        return new Company(name, domain, email, vatIdentificationNumber);
    }

    @Override
    public void generateName() {
        if (name != null) {
            return;
        }
        name = dataMaster.getRandomValue(COMPANY_NAME);
        if (baseProducer.trueOrFalse()) {
            name += " " + dataMaster.getRandomValue(COMPANY_SUFFIX);
        }
    }

    /**
     * Derives the domain from the company name. A trailing company suffix such as "GmbH &amp; Co. KG"
     * is dropped, and characters that are not allowed in a hostname are removed. Non-ASCII characters
     * are escaped and the result is truncated if it is too long (length &gt; 10) after escape.
     * <p>
     * It is compatible with other non-latin language and will not change the original result for latin language.
     * <p>
     * P.S. Actually the best way for Chinese here is to use phonetic writing (so as Japanese or Korean)
     */
    @Override
    public void generateDomain() {
        if (domain != null) {
            return;
        }

        String host = latinize(removeSuffix(name).toLowerCase(Locale.ROOT)).replaceAll("[\\p{ASCII}&&[^a-z0-9]]", "");
        int len1 = host.length();
        host = escapeNonAscii(host).replace("\\u", "");
        int len2 = host.length();
        if (len2 > len1 && len2 > 10) {
            host = host.substring(0, 10);
        }

        domain = host + "." + dataMaster.getRandomValue(DOMAIN);
    }

    /**
     * Removes the longest company suffix the given name ends with, if any.
     *
     * @param companyName the company name
     * @return the company name without its suffix
     */
    private String removeSuffix(String companyName) {
        return dataMaster.getStringList(COMPANY_SUFFIX).stream()
            .filter(suffix -> companyName.endsWith(" " + suffix))
            .max(Comparator.comparingInt(String::length))
            .map(suffix -> companyName.substring(0, companyName.length() - suffix.length() - 1))
            .orElse(companyName);
    }

    @Override
    public void generateEmail() {
        if (email != null) {
            return;
        }
        email = dataMaster.getRandomValue(COMPANY_EMAIL);
    }

    @Override
    public void generateVatIdentificationNumber() {
        if (vatIdentificationNumber != null) {
            return;
        }
        vatIdentificationNumber = vatIdentificationNumberProvider.get();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setVatIdentificationNumber(String vatIdentificationNumber) {
        this.vatIdentificationNumber = vatIdentificationNumber;
    }
}
