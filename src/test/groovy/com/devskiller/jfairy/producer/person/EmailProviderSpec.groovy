package com.devskiller.jfairy.producer.person

import spock.lang.Specification

import com.devskiller.jfairy.data.DataMaster
import com.devskiller.jfairy.producer.BaseProducer

import static com.devskiller.jfairy.producer.person.PersonProvider.PERSONAL_EMAIL

class EmailProviderSpec extends Specification {

    private DataMaster dataMaster = Mock(DataMaster)
    private BaseProducer baseProducer = Mock(BaseProducer)

    def setup() {
        dataMaster.getRandomValue(PERSONAL_EMAIL) >> "mail.com"
    }

    def "should remove spaces from firstName and lastName in email"() {
        given:
            baseProducer.randomBetween(1, 3) >> 1
            EmailProvider emailProvider = new EmailProvider(dataMaster, baseProducer, "Emilie Agneta", "Vojnov Allerstrand")

        when:
            String email = emailProvider.get()

        then:
            email == "emilieagnetavojnovallerstrand@mail.com"
    }

    def "should replace spaces with dots from firstName and lastName in email"() {
        given:
            baseProducer.randomBetween(1, 3) >> 2
            EmailProvider emailProvider = new EmailProvider(dataMaster, baseProducer, "Emilie Agneta", "Vojnov Allerstrand")

        when:
            String email = emailProvider.get()

        then:
            email == "emilie.agneta.vojnov.allerstrand@mail.com"
    }

    def "should replace spaces with dots from lastName in email"() {
        given:
            baseProducer.randomBetween(1, 3) >> 3
            EmailProvider emailProvider = new EmailProvider(dataMaster, baseProducer, "Emilie Agneta", "Vojnov Allerstrand")

        when:
            String email = emailProvider.get()

        then:
            email == "vojnovallerstrand@mail.com"
    }

    def "should strip accents from email"() {
        given:
            baseProducer.randomBetween(1, 3) >> 2
            EmailProvider emailProvider = new EmailProvider(dataMaster, baseProducer, "åäöéáąćęłńśóźż", "åäöéáąćęłńśóźż")

        when:
            String email = emailProvider.get()

        then:
            email == "aaoeaacelnsozz.aaoeaacelnsozz@mail.com"
    }

    def "should strip sharp s from email"() {
        given:
            baseProducer.randomBetween(1, 3) >> 2
            EmailProvider emailProvider = new EmailProvider(dataMaster, baseProducer, "Thieß", "Weißmann")

        when:
            String email = emailProvider.get()

        then:
            email == "thiess.weissmann@mail.com"
    }

    def "should remove apostrophes and spaces from email, variant #variant"() {
        given:
            baseProducer.randomBetween(1, 3) >> variant
            EmailProvider emailProvider = new EmailProvider(dataMaster, baseProducer, "Marco", "D'Angelo de Luca")

        when:
            String email = emailProvider.get()

        then:
            email == expected

        where:
            variant | expected
            1       | "marcodangelodeluca@mail.com"
            2       | "marco.dangelo.de.luca@mail.com"
            3       | "dangelodeluca@mail.com"
    }

    def "should keep names in non-latin scripts in email"() {
        given:
            baseProducer.randomBetween(1, 3) >> 3
            EmailProvider emailProvider = new EmailProvider(dataMaster, baseProducer, "ნინო", "ბაგრატიონი დავითაშვილი")

        when:
            String email = emailProvider.get()

        then:
            email == "ბაგრატიონიდავითაშვილი@mail.com"
    }
}
