package com.devskiller.jfairy

import spock.lang.Specification

import com.devskiller.jfairy.producer.person.Person

class FairyFrSpec extends Specification {

	private final int SEED = 7
	private Fairy fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.FRENCH).build()

	def "Should create French name"() {
		when:
			Person person = fairy.person();
		then:
			person.fullName == 'Flavien Garcia'
	}

	def "Should create French city"() {
		when:
			Person person = fairy.person();
		then:
			person.address.city == 'Saint-Herblain'
	}

}
