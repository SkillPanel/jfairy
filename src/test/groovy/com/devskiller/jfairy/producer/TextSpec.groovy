package com.devskiller.jfairy.producer

import spock.lang.Specification

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.text.TextProducer

class TextSpec extends Specification {

	private TextProducer text

	def setup() {
		text = Fairy.create().textProducer()
	}

	def "should instantiate Text producer with Text object"() {
		expect:
			text instanceof TextProducer
	}

	def "should be sure that data exists"() {
		expect:
			text.loremIpsum()
			text.text()
			text.word()
			text.word(100)
			text.latinWord()
			text.latinWord(100)
			text.sentence()
			text.sentence(100)
			text.latinSentence()
			text.latinSentence(100)
			text.paragraph()
			text.paragraph(100)
	}

	def "should limit generated text"() {
		when:
			text.limitedTo(10)
		then:
			text.loremIpsum().length() == 10
			text.word(100).length() == 10
			text.sentence(100).length() == 10
			text.paragraph(100).length() == 10
	}

	def "should generate Lorem Ipsum"() {
		expect:
			text.loremIpsum()
	}

	def "should generate localised text"() {
		expect:
			text.text()
	}

	def "should generate word"() {
		expect:
			text.word()
	}

	def "should generate latin word"() {
		expect:
			text.latinWord()
	}

	def "should generate sentence"() {
		expect:
			text.sentence()
	}

	def "should generate latin sentence"() {
		expect:
			text.latinSentence()
	}

	def "should generate paragraph"() {
		expect:
			text.paragraph()
	}

	def "should create random string"() {
		when:
			String randomString = text.randomString(10)
		then:
			randomString.length() == 10
	}

}
