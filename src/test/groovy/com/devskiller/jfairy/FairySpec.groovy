package com.devskiller.jfairy

import java.util.function.Supplier
import spock.lang.Specification

import com.devskiller.jfairy.data.DataMaster
import com.devskiller.jfairy.producer.person.Person

class FairySpec extends Specification {

	private static final String CUSTOM_STRING = 'Custom Data Master'

	DataMaster customDataMaster = Stub(DataMaster) {
		getString(_ as String) >> CUSTOM_STRING
		getStringList(_ as String) >> Arrays.asList(CUSTOM_STRING)
		getValuesOfType(_ as String, _ as String, String.class) >> CUSTOM_STRING
		getRandomValue(_ as String) >> CUSTOM_STRING
	}

	Supplier<DataMaster> customDataMasterProvider = Stub(Supplier) {
		get() >> customDataMaster
	}


	def "Second person should be different without fairy instance"() {

		given:
			Person person = Fairy.create().person();
		when:
			person = Fairy.create().person();
		then:
			person.fullName != old(person.fullName)
	}

	def "Second person should be different with one fairy"() {

		given:
			Fairy fairy = Fairy.create()
			Person person = fairy.person();
		when:
			person = fairy.person();
		then:
			person.fullName != old(person.fullName)
	}

	def "Second person should be the same with the same random seed"() {

		given:
			Fairy firstFairy = Fairy.builder().withRandomSeed(10).build()
			Fairy secondFairy = Fairy.builder().withRandomSeed(10).build()

			Person firstPerson = firstFairy.person()
			Person secondPerson = secondFairy.person()
			Person thirdPerson = firstFairy.person()
			Person fourthPerson = secondFairy.person()

		expect:
			firstPerson.fullName.equals(secondPerson.fullName)
			thirdPerson.fullName.equals(fourthPerson.fullName)

			!firstPerson.fullName.equals(thirdPerson.fullName)
	}

	def "Second person should be different with different random seeds"() {

		given:
			Fairy firstFairy = Fairy.builder().withRandomSeed(10).build()
			Fairy secondFairy = Fairy.builder().withRandomSeed(20).build()

			Person firstPerson = firstFairy.person()
			Person secondPerson = secondFairy.person();

		expect:
			!firstPerson.fullName.equals(secondPerson.fullName)
	}

	def "should use default DataMaster when custom not provided"() {
		given:
			Fairy fairy = Fairy.create();
		when:
			Person samplePerson = fairy.person()

		then:
			samplePerson.firstName && samplePerson.firstName != CUSTOM_STRING

	}

	def "should use custom DataMaster when provided"() {
		given:
			Fairy fairy = Fairy.create(customDataMasterProvider, Locale.forLanguageTag("EN"));

		when:
			Person samplePerson = fairy.person()

		then:
			samplePerson.firstName == CUSTOM_STRING

	}
}
