package com.devskiller.jfairy.producer.person.locale.br

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Person
import spock.lang.Specification

class BrAddressSpec extends Specification {

	private Fairy fairy = Fairy.create(Locale.forLanguageTag("br"))

	def "Should generate Brazilian address"() {
		when:
			Person person = fairy.person()
		then:
			person.address
			person.address.city
			person.address.street
			person.address.postalCode ==~ /\d{5}-\d{3}/
	}

	def "Should generate address in continental format"() {
		when:
			Person person = fairy.person()
			String line2 = person.address.addressLine2
		then:
			line2 ==~ /\d{5}-\d{3} .+/
	}
}
