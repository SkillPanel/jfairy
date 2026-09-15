package com.devskiller.jfairy.producer.person.locale.pl

import java.time.LocalDate

import spock.lang.Specification
import spock.lang.Unroll

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.DateProducer
import com.devskiller.jfairy.producer.person.NationalIdentificationNumber
import com.devskiller.jfairy.producer.person.Person

import static PlNationalIdentificationNumberProvider.isValid
import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.dateOfBirth
import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.sex

class PlNationalIdentificationNumberSpec extends Specification {

    private BaseProducer randomGenerator = Mock(BaseProducer)
    private DateProducer dateGenerator = Mock()

    @Unroll
    def "should validate #nationalIdentificationNumber as #valid"() {

        expect:
            isValid(nationalIdentificationNumber) == valid

        where:
            nationalIdentificationNumber | valid
            "13881939620"                | true
            "00421297469"                | true
            "99010147513"                | true
            "44051401359"                | true
            "44051401358"                | false
            "44051401458"                | true
            "44052401458"                | false
    }

    @Unroll
    def "should generate good nationalIdentificationNumber"() {

        expect:
            PlNationalIdentificationNumberProvider generator = new PlNationalIdentificationNumberProvider(dateGenerator,
                    randomGenerator, dateOfBirth(LocalDate.parse(date)), sex(Person.Sex.MALE))

            NationalIdentificationNumber nationalIdentificationNumber = generator.get()

            nationalIdentificationNumber.getValue().startsWith(prefix)
            isValid(nationalIdentificationNumber.getValue())

        where:
            date         | prefix
            "1999-01-01" | "990101"
            "1900-12-31" | "001231"
            "1800-01-31" | "008131"
            "2100-11-01" | "005101"
            "2199-01-11" | "994111"
    }
}
