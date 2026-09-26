package com.devskiller.jfairy.producer.person.locale.sk

import java.time.LocalDate

import spock.lang.Specification
import spock.lang.Unroll

import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.DateProducer
import com.devskiller.jfairy.producer.person.NationalIdentificationNumber
import com.devskiller.jfairy.producer.person.Person

import static SkNationalIdentificationNumberProvider.isValid
import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.dateOfBirth
import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.sex

class SkNationalIdentificationNumberSpec extends Specification {

    private BaseProducer randomGenerator = Mock(BaseProducer)
    private DateProducer dateGenerator = Mock()

    @Unroll
    def "should generate good nationalIdentificationNumber for #date"() {

        expect:
            SkNationalIdentificationNumberProvider generator = new SkNationalIdentificationNumberProvider(dateGenerator,
                    randomGenerator, dateOfBirth(LocalDate.parse(date)), sex(Person.Sex.MALE))

            NationalIdentificationNumber nationalIdentificationNumber = generator.get()

            nationalIdentificationNumber.getValue().startsWith(prefix)
            isValid(nationalIdentificationNumber.getValue())

        where:
            date         | prefix
            "1800-01-01" | "008101"
            "1999-01-01" | "990101"
            "2299-12-31" | "997231"
    }

    @Unroll
    def "should reject birth date #date outside supported range"() {

        when:
            new SkNationalIdentificationNumberProvider(dateGenerator, randomGenerator, dateOfBirth(LocalDate.parse(date)))

        then:
            IllegalArgumentException ex = thrown()
            ex.message == "Slovak national identification number supports birth dates from 1800 to 2299, got: " + date

        where:
            date << ["1799-12-31", "1700-01-01", "2300-01-01"]
    }
}
