package com.devskiller.jfairy

import java.time.LocalDate

import de.speedbanking.iban.IbanValidator
import spock.lang.Specification

import com.devskiller.jfairy.producer.company.locale.pl.PlVATIdentificationNumberProvider
import com.devskiller.jfairy.producer.payment.IBANProperties
import com.devskiller.jfairy.producer.person.Person
import com.devskiller.jfairy.producer.person.locale.pl.PlNationalIdentificationNumberProvider

import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.dateOfBirth
import static com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties.sex

class InvalidFairySpec extends Specification {

    private static final int SAMPLES = 200

    private Fairy plFairy = Fairy.create(Locale.forLanguageTag("pl"))

    def "should generate PESEL with wrong check digit"() {
        when:
            def numbers = (1..SAMPLES).collect { plFairy.invalid().nationalIdentificationNumber() }
        then:
            numbers.every { it ==~ /\d{11}/ }
            numbers.every { !PlNationalIdentificationNumberProvider.isValid(it) }
    }

    def "should keep date of birth and sex in invalid PESEL"() {
        when:
            def number = plFairy.invalid().nationalIdentificationNumber(
                dateOfBirth(LocalDate.of(1999, 1, 1)), sex(Person.Sex.MALE))
        then:
            number.startsWith("990101")
            Character.digit(number.charAt(9), 10) % 2 == 1
            !PlNationalIdentificationNumberProvider.isValid(number)
    }

    def "should reject invalid national identification number for locale without checksum"() {
        when:
            Fairy.create(Locale.ENGLISH).invalid().nationalIdentificationNumber()
        then:
            thrown(UnsupportedOperationException)
    }

    def "should generate NIP with wrong check digit"() {
        when:
            def numbers = (1..SAMPLES).collect { plFairy.invalid().vatIdentificationNumber() }
        then:
            numbers.every { it ==~ /\d{10}/ }
            numbers.every { !PlVATIdentificationNumberProvider.isValid(it) }
    }

    def "should generate IBAN with wrong check digits"() {
        when:
            def ibans = (1..SAMPLES).collect { plFairy.invalid().iban(IBANProperties.country("DE")) }
        then:
            ibans.every { it.ibanNumber ==~ /DE\d{20}/ }
            ibans.every { it.ibanNumber == "DE" + it.checkDigit + it.bban }
            ibans.every { !IbanValidator.isMod97Valid(it.ibanNumber) }
    }

    def "should return null invalid IBAN for country without IBAN"() {
        expect:
            plFairy.invalid().iban(IBANProperties.country("US")) == null
    }

    def "should reject invalid VAT identification number for locale without checksum"() {
        when:
            Fairy.create(Locale.ENGLISH).invalid().vatIdentificationNumber()
        then:
            thrown(UnsupportedOperationException)
    }
}
