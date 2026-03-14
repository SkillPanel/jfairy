package com.devskiller.jfairy;

import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.company.CompanyProperties;
import com.devskiller.jfairy.producer.payment.CreditCard;
import com.devskiller.jfairy.producer.payment.IBAN;
import com.devskiller.jfairy.producer.payment.IBANProperties;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.PersonProperties;

/**
 * Convenience wrapper around {@link Fairy} that ensures generated objects
 * are unique by their natural key (email for Person, name for Company, etc.).
 *
 * <pre>{@code
 * Fairy fairy = Fairy.create();
 * UniqueFairy unique = fairy.unique();
 *
 * Person p1 = unique.person();  // unique by email
 * Person p2 = unique.person();  // different email than p1
 * Company c = unique.company(); // unique by name
 *
 * unique.reset(); // clear all tracked values
 * }</pre>
 */
public class UniqueFairy {

	private final Fairy fairy;
	private final UniqueEnforcer<Person> personEnforcer;
	private final UniqueEnforcer<Company> companyEnforcer;
	private final UniqueEnforcer<IBAN> ibanEnforcer;
	private final UniqueEnforcer<CreditCard> creditCardEnforcer;

	UniqueFairy(Fairy fairy, int maxRetries) {
		this.fairy = fairy;
		this.personEnforcer = UniqueEnforcer.of(fairy::person, Person::getEmail, maxRetries);
		this.companyEnforcer = UniqueEnforcer.of(fairy::company, Company::getName, maxRetries);
		this.ibanEnforcer = UniqueEnforcer.of(() -> fairy.iban(), IBAN::getIbanNumber, maxRetries);
		this.creditCardEnforcer = UniqueEnforcer.of(fairy::creditCard, CreditCard::getCardNumber, maxRetries);
	}

	public Person person(PersonProperties.PersonProperty... personProperties) {
		if (personProperties.length == 0) {
			return personEnforcer.next();
		}
		return UniqueEnforcer.of(() -> fairy.person(personProperties), Person::getEmail).next();
	}

	public Company company(CompanyProperties.CompanyProperty... companyProperties) {
		if (companyProperties.length == 0) {
			return companyEnforcer.next();
		}
		return UniqueEnforcer.of(() -> fairy.company(companyProperties), Company::getName).next();
	}

	public IBAN iban(IBANProperties.Property... properties) {
		if (properties.length == 0) {
			return ibanEnforcer.next();
		}
		return UniqueEnforcer.of(() -> fairy.iban(properties), IBAN::getIbanNumber).next();
	}

	public CreditCard creditCard() {
		return creditCardEnforcer.next();
	}

	public void reset() {
		personEnforcer.reset();
		companyEnforcer.reset();
		ibanEnforcer.reset();
		creditCardEnforcer.reset();
	}
}
