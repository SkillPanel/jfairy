package com.devskiller.jfairy.producer.net

import org.apache.commons.validator.routines.InetAddressValidator
import spock.lang.Specification

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.RandomGenerator

class NetworkSpec extends Specification {

	private InetAddressValidator ipValidator = InetAddressValidator.getInstance()

	private IPNumberProducer ipNumber = new IPNumberProducer(new BaseProducer(new RandomGenerator()))
	private NetworkProducer network = new NetworkProducer(ipNumber)

	def "Should generate proper ip number"() {
		String ip = network.ipAddress()

		expect:
			ip
			ipValidator.isValid(ip)

	}

	def "Should generate proper url"() {
		String url = network.url(true)

		expect:
			url.startsWith("https://") && url.endsWith(".com")

	}
}
