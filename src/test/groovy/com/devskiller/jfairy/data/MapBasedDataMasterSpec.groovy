/*
 * Copyright (c) 2013. Codearte
 */

package com.devskiller.jfairy.data

import spock.lang.Specification

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator
import com.devskiller.jfairy.producer.person.PersonProvider

class MapBasedDataMasterSpec extends Specification {

	private BaseProducer baseProducer = Spy(BaseProducer, constructorArgs: [new RandomGenerator()])
	private MapBasedDataMaster data = Spy(MapBasedDataMaster, constructorArgs: [baseProducer])

	def setup() {
		baseProducer.randomBetween() >> 0
	}

	def "should read first names"() {
		when:
			DataMaster dataMaster = new MapBasedDataMaster()
			dataMaster.readResources("jfairy_en.yml")

			Map<String, List<String>> firstNames = dataMaster.getData(PersonProvider.FIRST_NAME, Map.class)
		then:
			firstNames.size() > 0
			firstNames.keySet().size() > 0
	}

	def "should return men"() {
		setup:
			data.getData(PersonProvider.FIRST_NAME, Map.class) >> [female: ['Ana', 'Ivon'], male: ['Mark']]

		when:
			String male = data.getValuesOfType(PersonProvider.FIRST_NAME, "male", String.class)

		then:
			male == "Mark"
	}

	def "should return one of women"() {
		setup:
			data.getData(PersonProvider.FIRST_NAME, Map.class) >> [female: ['Ana', 'Ivon'], male: ['Mark']]

		when:
			String female = data.getValuesOfType(PersonProvider.FIRST_NAME, "female", String.class)

		then:
			(female == "Ana") || (female == "Ivon")
	}

}
