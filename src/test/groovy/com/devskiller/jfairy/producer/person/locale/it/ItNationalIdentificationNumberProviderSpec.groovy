package com.devskiller.jfairy.producer.person.locale.it

import java.time.LocalDate

import spock.lang.Specification
import spock.lang.Unroll

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.data.DataMaster
import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.DateProducer
import com.devskiller.jfairy.producer.person.Person

import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.dateOfBirth
import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.firstName
import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.lastName
import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.sex
import static com.devskiller.jfairy.producer.person.locale.it.ItNationalIdentificationNumberProvider.isValid

class ItNationalIdentificationNumberProviderSpec extends Specification {

    private BaseProducer baseProducer = Stub(BaseProducer) {
        randomElement(_ as List) >> "F205"
    }

    private ItNationalIdentificationNumberFactory factory =
        new ItNationalIdentificationNumberFactory(Stub(DataMaster), baseProducer, Stub(DateProducer))

    @Unroll
    def "should validate #taxCode as #valid"() {
        expect:
            isValid(taxCode) == valid

        where:
            taxCode            | valid
            "MRTMTT25D09F205Z" | true
            "RSSMRA80A01H501U" | true
            "MRTMTT25D09F205A" | false
            "MRTMTT25D09F206Z" | false
            "mrtmtt25d09f205z" | false
            "MRTMTT25D09F205"  | false
            "MRTMTT25Z09F205Z" | false
    }

    def "should generate the tax code of the documented example"() {
        when:
            def taxCode = factory.produceNationalIdentificationNumberProvider(
                firstName("Matteo"), lastName("Moretti"), dateOfBirth(LocalDate.of(1925, 4, 9)), sex(Person.Sex.MALE))
                .get().value
        then:
            taxCode == "MRTMTT25D09F205Z"
    }

    def "should add 40 to the day of birth for women"() {
        when:
            def taxCode = factory.produceNationalIdentificationNumberProvider(
                firstName("Maria"), lastName("Rossi"), dateOfBirth(LocalDate.of(1990, 5, 17)), sex(Person.Sex.FEMALE))
                .get().value
        then:
            taxCode.startsWith("RSSMRA90E57F205")
            isValid(taxCode)
    }

    @Unroll
    def "should encode last name #name as #code"() {
        expect:
            ItNationalIdentificationNumberProvider.lastNameCode(name) == code

        where:
            name      | code
            "Rossi"   | "RSS"
            "Moretti" | "MRT"
            "De Luca" | "DLC"
            "D'Amico" | "DMC"
            "Rè"      | "REX"
            "Fo"      | "FOX"
    }

    @Unroll
    def "should encode first name #name as #code"() {
        expect:
            ItNationalIdentificationNumberProvider.firstNameCode(name) == code

        where:
            name           | code
            "Matteo"       | "MTT"
            "Gianfranco"   | "GFR"
            "Luca"         | "LCU"
            "Niccolò"      | "NCL"
            "Ugo"          | "GUO"
            "Al"           | "LAX"
            "Maria Giulia" | "MGL"
    }

    def "should always generate valid tax codes"() {
        given:
            def fairy = Fairy.create(Locale.ITALIAN)
        when:
            def taxCodes = (1..200).collect { fairy.person().nationalIdentificationNumber }
        then:
            taxCodes.every { isValid(it) }
    }

    def "should derive the tax code of a person from name, birth date and sex"() {
        when:
            def persons = (1..50).collect { Fairy.create(Locale.ITALIAN).person() }
        then:
            persons.every { person ->
                def taxCode = person.nationalIdentificationNumber
                taxCode.substring(0, 3) == ItNationalIdentificationNumberProvider.lastNameCode(person.lastName) &&
                    taxCode.substring(3, 6) == ItNationalIdentificationNumberProvider.firstNameCode(person.firstName + " " + person.middleName) &&
                    taxCode.substring(6, 8) == String.format("%02d", person.dateOfBirth.year % 100) &&
                    Integer.parseInt(taxCode.substring(9, 11)) == person.dateOfBirth.dayOfMonth + (person.female ? 40 : 0)
            }
    }
}
