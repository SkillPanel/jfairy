package com.devskiller.jfairy

import java.time.LocalDate

import de.speedbanking.iban.IbanValidator
import spock.lang.Specification

import com.devskiller.jfairy.producer.VATIdentificationNumberProvider
import com.devskiller.jfairy.producer.company.locale.pl.PlVATIdentificationNumberProvider
import com.devskiller.jfairy.producer.company.locale.sk.SkVATIdentificationNumberProvider
import com.devskiller.jfairy.producer.company.locale.sv.SvVATIdentificationNumberProvider
import com.devskiller.jfairy.producer.payment.IBANProperties
import com.devskiller.jfairy.producer.person.Person
import com.devskiller.jfairy.producer.person.PersonProperties
import com.devskiller.jfairy.producer.person.locale.pl.PlNationalIdentificationNumberProvider
import com.devskiller.jfairy.producer.person.locale.sk.SkNationalIdentificationNumberProvider
import com.devskiller.jfairy.producer.person.locale.sv.SvNationalIdentificationNumberProvider

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

    def "should build person whose birth date and sex match the invalid PESEL"() {
        given:
            def birthDate = LocalDate.of(1990, 5, 17)
            def pesel = plFairy.invalid().nationalIdentificationNumber(dateOfBirth(birthDate), sex(Person.Sex.FEMALE))
        when:
            def person = plFairy.person(PersonProperties.female(), PersonProperties.withDateOfBirth(birthDate),
                PersonProperties.withNationalIdentificationNumber(pesel))
        then:
            person.nationalIdentificationNumber == pesel
            person.dateOfBirth == birthDate
            person.female
            pesel.startsWith("900517")
    }

    def "should generate Slovak national identification number with wrong check digit"() {
        when:
            def numbers = (1..SAMPLES).collect {
                Fairy.create(Locale.forLanguageTag("sk")).invalid().nationalIdentificationNumber()
            }
        then:
            numbers.every { it ==~ /\d{11}/ }
            numbers.every { !SkNationalIdentificationNumberProvider.isValid(it) }
    }

    def "should generate Swedish personal identity number with wrong check digit"() {
        when:
            def numbers = (1..SAMPLES).collect {
                Fairy.create(Locale.forLanguageTag("sv")).invalid().nationalIdentificationNumber()
            }
        then:
            numbers.every { it ==~ /\d{6}-\d{4}/ }
            numbers.every { !SvNationalIdentificationNumberProvider.isValid(it) }
    }

    def "should generate Slovak VAT identification number with wrong check digit"() {
        when:
            def numbers = (1..SAMPLES).collect {
                Fairy.create(Locale.forLanguageTag("sk")).invalid().vatIdentificationNumber()
            }
        then:
            numbers.every { it ==~ /\d{10}/ }
            numbers.every { !SkVATIdentificationNumberProvider.isValid(it) }
    }

    def "should generate Swedish VAT identification number with wrong check digit"() {
        when:
            def numbers = (1..SAMPLES).collect {
                Fairy.create(Locale.forLanguageTag("sv")).invalid().vatIdentificationNumber()
            }
        then:
            numbers.every { it ==~ /SE\d{10}01/ }
            numbers.every { !SvVATIdentificationNumberProvider.isValid(it) }
    }

    def "should name the provider class when invalid numbers are not supported"() {
        given:
            def provider = new VATIdentificationNumberProvider() {
                @Override
                String get() {
                    return "123"
                }
            }
        when:
            provider.getInvalid()
        then:
            def e = thrown(UnsupportedOperationException)
            e.message.endsWith(provider.getClass().getName())
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
