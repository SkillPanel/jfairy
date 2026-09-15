package com.devskiller.jfairy.producer.company;

import java.util.StringJoiner;

public class Company {

    private final String name;
    private final String domain;
    private final String email;
    private final String vatIdentificationNumber;

    public Company(String name, String domain, String email, String vatIdentificationNumber) {
        this.name = name;
        this.domain = domain;
        this.email = email;
        this.vatIdentificationNumber = vatIdentificationNumber;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return "http://www." + domain;
    }

    public String getEmail() {
        return email + "@" + domain;
    }

    public String getDomain() {
        return domain;
    }

    public String getVatIdentificationNumber() {
        return vatIdentificationNumber;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
            .add("name='" + name + "'")
            .add("url=" + getUrl())
            .add("email=" + getEmail())
            .add("vatIdentificationNumber=" + vatIdentificationNumber)
            .toString();
    }

}
