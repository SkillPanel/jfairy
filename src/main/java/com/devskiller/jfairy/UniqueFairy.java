package com.devskiller.jfairy;

import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.company.CompanyProperties;
import com.devskiller.jfairy.producer.payment.CreditCard;
import com.devskiller.jfairy.producer.payment.IBAN;
import com.devskiller.jfairy.producer.payment.IBANProperties;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.PersonProperties;

/**
 * Convenience wrapper that ensures generated objects are unique by their
 * natural key (email for Person, name for Company, etc.).
 *
 * <p>Uniqueness is tracked per entity type. A person email will not conflict
 * with a company name. Call {@link #reset()} to clear all tracked values.</p>
 *
 * <p>This class is not thread-safe. Store the reference and reuse it —
 * calling {@code fairy.unique()} in a loop creates independent instances
 * with no shared tracking.</p>
 *
 * <pre>{@code
 * UniqueFairy unique = fairy.unique();
 * Person p1 = unique.person();  // unique by email
 * Person p2 = unique.person();  // different email than p1
 * Company c = unique.company(); // unique by name
 * unique.reset();
 * }</pre>
 */
public final class UniqueFairy {

	private final Fairy fairy;
	private final UniqueEnforcer<Person> personEnforcer;
	private final UniqueEnforcer<Company> companyEnforcer;
	private final UniqueEnforcer<IBAN> ibanEnforcer;
	private final UniqueEnforcer<CreditCard> creditCardEnforcer;

	UniqueFairy(Fairy fairy, int maxRetries) {
		this.fairy = fairy;
		this.personEnforcer = UniqueEnforcer.of(fairy::person, Person::getEmail, maxRetries);
		this.companyEnforcer = UniqueEnforcer.of(fairy::company, Company::getName, maxRetries);
		// Lambda needed: fairy::iban is ambiguous (overloaded no-arg and vararg)
		this.ibanEnforcer = UniqueEnforcer.of(() -> fairy.iban(), IBAN::getIbanNumber, maxRetries);
		this.creditCardEnforcer = UniqueEnforcer.of(fairy::creditCard, CreditCard::getCardNumber, maxRetries);
	}

	public Person person(PersonProperties.PersonProperty... personProperties) {
		if (personProperties.length == 0) {
			return personEnforcer.next();
		}
		return personEnforcer.next(() -> fairy.person(personProperties));
	}

	public Company company(CompanyProperties.CompanyProperty... companyProperties) {
		if (companyProperties.length == 0) {
			return companyEnforcer.next();
		}
		return companyEnforcer.next(() -> fairy.company(companyProperties));
	}

	public IBAN iban(IBANProperties.Property... properties) {
		if (properties.length == 0) {
			return ibanEnforcer.next();
		}
		return ibanEnforcer.next(() -> fairy.iban(properties));
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
