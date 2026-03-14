package com.devskiller.jfairy

import spock.lang.Specification

class UniqueFairySpec extends Specification {

	private Fairy fairy = Fairy.create()

	def "should generate unique persons by email"() {
		given:
			def unique = fairy.unique()
		when:
			def persons = (1..50).collect { unique.person() }
			def emails = persons.collect { it.email }
		then:
			emails.toSet().size() == 50
	}

	def "should generate unique companies by name"() {
		given:
			def unique = fairy.unique()
		when:
			def companies = (1..20).collect { unique.company() }
			def names = companies.collect { it.name }
		then:
			names.toSet().size() == 20
	}

	def "should generate unique IBANs"() {
		given:
			def plFairy = Fairy.create(Locale.forLanguageTag("pl"))
			def unique = plFairy.unique()
		when:
			def ibans = (1..10).collect { unique.iban() }
			def numbers = ibans.collect { it.ibanNumber }
		then:
			numbers.toSet().size() == 10
	}

	def "should generate unique credit cards"() {
		given:
			def unique = fairy.unique()
		when:
			def cards = (1..10).collect { unique.creditCard() }
			def numbers = cards.collect { it.cardNumber }
		then:
			numbers.toSet().size() == 10
	}

	def "should reset all trackers"() {
		given:
			def unique = fairy.unique()
			(1..5).each { unique.person() }
			(1..5).each { unique.company() }
		when:
			unique.reset()
			unique.person()
			unique.company()
		then:
			noExceptionThrown()
	}

	def "should support custom max retries"() {
		given:
			def unique = fairy.unique(500)
		when:
			unique.person()
		then:
			noExceptionThrown()
	}
}
