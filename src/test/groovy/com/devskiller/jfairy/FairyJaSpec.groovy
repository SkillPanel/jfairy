package com.devskiller.jfairy

import spock.lang.Specification

import com.devskiller.jfairy.producer.company.Company
import com.devskiller.jfairy.producer.person.Person

class FairyJaSpec extends Specification {
    private final int SEED = 1
    private Fairy fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.JAPAN).build()

    /********************
     * Person
     */
    def "Should create Japanese name in family-name-first order"() {
        when:
        Person person = fairy.person()
        then:
        person.fullName == '松尾 坂'
        person.fullName == person.lastName + " " + person.firstName
    }

    def "Should create Japanese my number id card"() {
        when:
        Person person = fairy.person()
        then:
        person.nationalIdentityCardNumber.length() == 12
    }

    def "Should create empty national identification number since My Number is the only citizen id in Japan"() {
        when:
        Person person = fairy.person()
        then:
        person.nationalIdentificationNumber.length() == 0
    }

    def "Should create Japanese address"() {
        when:
        Person person = fairy.person()
        then:
        person.address.addressLine2 == '宇部中央区2-27-11'
    }

    def "Should create Japanese city"() {
        when:
        Person person = fairy.person()
        then:
        person.address.city == '宇部'
    }

    def "Should create Japanese passport number"() {
        when:
        Person person = fairy.person()
        then:
        person.passportNumber ==~ /[A-Z]{2}\d{7}/
    }

    /********************
     * Company
     */
    def "Should create Japanese company name"() {
        when:
        Company company = fairy.company()
        then:
        company.name == "白鳥製作所"
    }

    def "Should create Japanese company url with a readable romaji domain"() {
        when:
        Company company = fairy.company()
        then:
        company.url == "http://www.hakucho-ss.or.jp"
    }

    def "Should create Japanese company email with a readable romaji domain"() {
        when:
        Company company = fairy.company()
        then:
        company.email == "office@hakucho-ss.or.jp"
    }

    def "Should create Japanese company invoice registration number"() {
        when:
        Company company = fairy.company()
        then:
        company.vatIdentificationNumber ==~ /T\d{13}/
    }
}
