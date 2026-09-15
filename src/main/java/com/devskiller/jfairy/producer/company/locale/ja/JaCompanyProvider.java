package com.devskiller.jfairy.producer.company.locale.ja;

import java.util.List;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.company.CompanyProperties;
import com.devskiller.jfairy.producer.company.DefaultCompanyProvider;

/**
 * Japanese company provider. The default {@link DefaultCompanyProvider#generateDomain()} escapes every
 * non-ASCII character of the company name to its hex code point, which turns a Kanji/Katakana name into an
 * unreadable host such as {@code 767d9ce588.co.jp}. This provider instead picks the company name and its
 * domain together: {@code companyNames} and {@code companyDomains} in {@code jfairy_ja.yml} are kept in the
 * same order, so the same index yields a matching, readable romaji domain, e.g. {@code 北斗テクノロジー} with
 * {@code hokuto-technology}.
 */
public class JaCompanyProvider extends DefaultCompanyProvider {

    private static final String COMPANY_DOMAIN_HOST = "companyDomains";

    /**
     * Index picked in {@link #generateName()}, reused in {@link #generateDomain()} so the domain matches the
     * chosen name. Stays {@code -1} when the name was supplied externally (e.g. via {@link CompanyProperties}),
     * in which case the domain falls back to an independently picked, still-readable stem.
     */
    private int selectedIndex = -1;

    public JaCompanyProvider(BaseProducer baseProducer, DataMaster dataMaster,
                              VATIdentificationNumberProvider vatIdentificationNumberProvider,
                              CompanyProperties.CompanyProperty... companyProperties) {
        super(baseProducer, dataMaster, vatIdentificationNumberProvider, companyProperties);
    }

    @Override
    public void generateName() {
        if (name != null) {
            return;
        }
        List<String> names = dataMaster.getStringList(COMPANY_NAME);
        selectedIndex = baseProducer.randomBetween(0, names.size() - 1);
        name = names.get(selectedIndex);
        if (baseProducer.trueOrFalse()) {
            name += " " + dataMaster.getRandomValue(COMPANY_SUFFIX);
        }
    }

    @Override
    public void generateDomain() {
        if (domain != null) {
            return;
        }
        List<String> domains = dataMaster.getStringList(COMPANY_DOMAIN_HOST);
        String host = selectedIndex >= 0 && selectedIndex < domains.size()
            ? domains.get(selectedIndex)
            : dataMaster.getRandomValue(COMPANY_DOMAIN_HOST);
        domain = host + "." + dataMaster.getRandomValue(DOMAIN);
    }
}
