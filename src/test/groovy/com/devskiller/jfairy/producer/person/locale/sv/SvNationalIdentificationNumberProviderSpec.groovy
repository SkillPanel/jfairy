package com.devskiller.jfairy.producer.person.locale.sv

import java.time.LocalDate

import spock.lang.Specification
import spock.lang.Unroll

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.DateProducer
import com.devskiller.jfairy.producer.person.NationalIdentificationNumber
import com.devskiller.jfairy.producer.person.Person

import static SvNationalIdentificationNumberProvider.isValid
import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.dateOfBirth
import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.sex

class SvNationalIdentificationNumberProviderSpec extends Specification {

    private BaseProducer randomGenerator = Mock(BaseProducer)
    private DateProducer dateGenerator = Mock(DateProducer)

    @Unroll
    def "should validate #nationalIdentificationNumber as #valid"() {

        expect:
            isValid(nationalIdentificationNumber) == valid

        where:
            nationalIdentificationNumber | valid
            "370612-1450"                | true
            "370612-9529"                | true
            "640401-1550"                | true
            "690509-3966"                | true
            "870727-2441"                | false
            "880418-9043"                | false
            "610219-6694"                | false
    }

    @Unroll
    def "should generate good nationalIdentificationNumber"() {

        expect:
            SvNationalIdentificationNumberProvider generator = new SvNationalIdentificationNumberProvider(dateGenerator,
                    randomGenerator, dateOfBirth(LocalDate.parse(date)), sex(Person.Sex.MALE))

            NationalIdentificationNumber nationalIdentificationNumber = generator.get()

            nationalIdentificationNumber.getValue().startsWith(prefix)
            isValid(nationalIdentificationNumber.getValue())

        where:
            date         | prefix
            "1999-01-01" | "990101"
            "1950-12-31" | "501231"
            "1910-01-31" | "100131"
            "2100-11-01" | "001101"
            "2199-01-22" | "990122"
    }
}
