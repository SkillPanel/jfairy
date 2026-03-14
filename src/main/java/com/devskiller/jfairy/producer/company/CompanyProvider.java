package com.devskiller.jfairy.producer.company;
import java.util.function.Supplier;


public interface CompanyProvider extends Supplier<Company> {

	String DOMAIN = "domains";
	String COMPANY_SUFFIX = "companySuffixes";
	String COMPANY_NAME = "companyNames";
	String COMPANY_EMAIL = "companyEmails";

	@Override
	Company get();

	void generateName();

	void generateDomain();

	void generateEmail();

	void generateVatIdentificationNumber();

	void setName(String name);

	void setDomain(String domain);

	void setEmail(String email);

	void setVatIdentificationNumber(String vatIdentificationNumber);
}
