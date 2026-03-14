package com.devskiller.jfairy

import spock.lang.Specification

import com.devskiller.jfairy.producer.person.Person

/**
 * @author Roland Weisleder
 */
class FairyDeSpec extends Specification {

	private final int SEED = 186
	private Fairy fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.GERMAN).build()

	def "Should create German name"() {
		when:
			Person person = fairy.person();
		then:
			person.fullName == 'Hannsgeorg Scheidt' // well, that's german enough...
	}

	def "Should create German city"() {
		when:
			Person person = fairy.person();
		then:
			person.address.city == 'Unterbachern'
	}

}
