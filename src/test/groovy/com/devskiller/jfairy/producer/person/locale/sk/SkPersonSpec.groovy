package com.devskiller.jfairy.producer.person.locale.sk

import spock.lang.Specification

import com.devskiller.jfairy.Bootstrap
import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Person

class SkPersonSpec extends Specification {

	private Fairy fairy = Bootstrap.create(Locale.forLanguageTag("SK"))

	def setup() {
		Bootstrap.create()
	}

	def "should create nationality"() {
		when:
			Person person = fairy.person()
		then:
			person.nationality.code == 'SK'
	}

	def "should create nationalIdentificationNumber"() {
		when:
			Person person = fairy.person()
		then:
			person.nationalIdentificationNumber
	}

	def "should create person with first name"() {
		when:
			Person person = fairy.person()
		then:
			person.firstName
	}

	def "should create person with last name"() {
		when:
			Person person = fairy.person()
		then:
			person.lastName
	}

	def "should create person with email"() {
		when:
			Person person = fairy.person()
		then:
			person.email
	}

}
