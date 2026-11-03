package com.devskiller.jfairy.producer.person;

import java.util.function.Supplier;

import com.devskiller.jfairy.producer.company.Company;

import static com.devskiller.jfairy.producer.util.StringUtils.latinize;
import static com.devskiller.jfairy.producer.util.StringUtils.lowerCase;
import static com.devskiller.jfairy.producer.util.StringUtils.toLoginName;

public class CompanyEmailProvider implements Supplier<String> {

    private final String firstName;
    private final String lastName;
    private final Company company;

    public CompanyEmailProvider(String firstName, String lastName, Company company) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
    }

    @Override
    public String get() {
        String localPart = (firstName + '.' + lastName).replace(" ", ".");
        return toLoginName(localPart) + '@' + latinize(lowerCase(company.getDomain()));
    }
}
