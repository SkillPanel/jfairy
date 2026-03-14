package com.devskiller.jfairy.producer.company;


import org.apache.commons.lang3.StringUtils;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.VATIdentificationNumberProvider;
import com.devskiller.jfairy.producer.util.TextUtils;

public class DefaultCompanyProvider implements CompanyProvider {

	protected String name;
	protected String domain;
	protected String email;
	protected String vatIdentificationNumber;

	protected BaseProducer baseProducer;
	protected DataMaster dataMaster;

	protected VATIdentificationNumberProvider vatIdentificationNumberProvider;


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
	 * In case of the illegal hostname characters in company name
	 * and truncate it if it is too long (length &gt; 10) after escape
	 *
	 * It is compatible with other non-latin language and will not change the original result for latin language.
	 *
	 * P.S. Actually the best way for Chinese here is to use phonetic writing (so as Japanese or Korean)
	 */
	@Override
	public void generateDomain() {
		if (domain != null) {
			return;
		}

		String host = TextUtils.stripAccents(StringUtils.strip(StringUtils.deleteWhitespace(name.toLowerCase()), ".").replace("/", ""));
		int len1 = host.length();
		host = TextUtils.escapeNonAscii(host).replaceAll("\\\\u", "");
		int len2 = host.length();
		if (len2 > len1 && len2 > 10)
			host = host.substring(0, 10);

		domain = host + "." + dataMaster.getRandomValue(DOMAIN);
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
