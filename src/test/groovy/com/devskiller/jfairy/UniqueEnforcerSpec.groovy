package com.devskiller.jfairy

import com.devskiller.jfairy.producer.person.Person
import com.devskiller.jfairy.producer.company.Company
import spock.lang.Specification

class UniqueEnforcerSpec extends Specification {

	private Fairy fairy = Fairy.create()

	def "should generate unique persons by email"() {
		given:
			def unique = UniqueEnforcer.of(fairy.&person, { Person p -> p.email })
		when:
			def persons = (1..50).collect { unique.next() }
			def emails = persons.collect { it.email }
		then:
			emails.toSet().size() == 50
	}

	def "should generate unique companies by name"() {
		given:
			def unique = UniqueEnforcer.of(fairy.&company, { Company c -> c.name })
		when:
			def companies = (1..20).collect { unique.next() }
			def names = companies.collect { it.name }
		then:
			names.toSet().size() == 20
	}

	def "should throw after max retries with small pool"() {
		given:
			int counter = 0
			def unique = UniqueEnforcer.of({ -> counter++ % 3 }, { it }, 100)
		when:
			unique.next() // 0
			unique.next() // 1
			unique.next() // 2
			unique.next() // should fail - only 3 unique values possible
		then:
			thrown(UniqueGenerationException)
	}

	def "should reset tracked values"() {
		given:
			int counter = 0
			def unique = UniqueEnforcer.of({ -> counter++ % 2 }, { it }, 100)
		when:
			unique.next() // 0
			unique.next() // 1
			unique.reset()
			unique.next() // 0 again - OK after reset
		then:
			unique.size() == 1
	}

	def "should track size"() {
		given:
			def unique = UniqueEnforcer.of(fairy.&person, { Person p -> p.email })
		when:
			(1..10).each { unique.next() }
		then:
			unique.size() == 10
	}

	def "should allow custom max retries"() {
		given:
			def unique = UniqueEnforcer.of({ -> "same" }, { it }, 5)
		when:
			unique.next() // OK first time
			unique.next() // should fail after 5 retries
		then:
			def e = thrown(UniqueGenerationException)
			e.message.contains("5 retries")
	}

	def "should share seen set between default and custom suppliers"() {
		given:
			int counter = 0
			def unique = UniqueEnforcer.of({ -> counter++ }, { it }, 100)
		when:
			unique.next()                          // 0 via default
			unique.next({ -> counter++ } as java.util.function.Supplier)  // 1 via custom
			unique.next()                          // 2 via default
		then:
			unique.size() == 3
	}

	def "should reject zero maxRetries"() {
		when:
			UniqueEnforcer.of({ -> "x" }, { it }, 0)
		then:
			thrown(IllegalArgumentException)
	}

	def "should reject negative maxRetries"() {
		when:
			UniqueEnforcer.of({ -> "x" }, { it }, -1)
		then:
			thrown(IllegalArgumentException)
	}

	def "should reject null generator"() {
		when:
			UniqueEnforcer.of(null, { it })
		then:
			thrown(NullPointerException)
	}

	def "should reject null keyExtractor"() {
		when:
			UniqueEnforcer.of({ -> "x" }, null)
		then:
			thrown(NullPointerException)
	}
}
