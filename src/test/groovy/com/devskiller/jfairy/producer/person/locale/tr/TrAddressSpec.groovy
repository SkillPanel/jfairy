package com.devskiller.jfairy.producer.person.locale.tr

import spock.lang.Specification

class TrAddressSpec extends Specification {

	def "should format Turkish address correctly"() {
		given:
		TrAddress address = new TrAddress("10", "İstiklal Caddesi", "5", "34000", "İstanbul")

		expect:
		address.getAddressLine1() == "İstiklal Caddesi No: 10 Daire: 5"
		address.getAddressLine2() == "34000 İstanbul"
	}
}
