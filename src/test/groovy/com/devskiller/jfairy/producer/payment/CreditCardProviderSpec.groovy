package com.devskiller.jfairy.producer.payment

import java.time.LocalDateTime

import org.apache.commons.validator.routines.CreditCardValidator
import org.apache.commons.validator.routines.CreditCardValidator.CreditCardRange
import spock.lang.Specification

import com.devskiller.jfairy.data.DataMaster
import com.devskiller.jfairy.data.MapBasedDataMaster
import com.devskiller.jfairy.producer.BaseProducer
import com.devskiller.jfairy.producer.DateProducer
import com.devskiller.jfairy.producer.RandomGenerator

/**
 * @author jkubrynski@gmail.com
 * @since 2013-11-17
 */
class CreditCardProviderSpec extends Specification {

    public static final LocalDateTime EXPIRY_DATE = LocalDateTime.parse("2009-02-11T23:59:59.999")

    private DataMaster dataMaster
    private DateProducer dateProducer
    private CreditCardProvider creditCardProvider
    private BaseProducer baseProducer

    def setup() {
        baseProducer = new BaseProducer(new RandomGenerator())
        dataMaster = new MapBasedDataMaster(baseProducer)
        dateProducer = Mock(DateProducer)
        dataMaster.readResources("jfairy.properties")
        creditCardProvider = new CreditCardProvider(dataMaster, baseProducer, dateProducer)
    }

    def "should generate valid #vendor card"() {
        when:
            List<CreditCard> cards = (1..200).collect { creditCardProvider.get(vendor) }
        then:
            cards.every { it.vendor == displayName }
            cards.every { it.cardNumber.length() == numberLength }
            cards.every { validator.isValid(it.cardNumber) }
            cards.every { it.cvv ==~ /\d{$cvvLength}/ }
        where:
            vendor                            | displayName        | numberLength | cvvLength | validator
            CreditCardVendor.VISA             | "Visa"             | 16           | 3         | new CreditCardValidator(CreditCardValidator.VISA)
            CreditCardVendor.MASTERCARD       | "Mastercard"       | 16           | 3         | new CreditCardValidator(CreditCardValidator.MASTERCARD)
            CreditCardVendor.AMERICAN_EXPRESS | "American Express" | 15           | 4         | new CreditCardValidator(CreditCardValidator.AMEX)
            CreditCardVendor.DISCOVER         | "Discover"         | 16           | 3         | new CreditCardValidator(CreditCardValidator.DISCOVER)
            // commons-validator has no JCB constant, its IIN range is 3528-3589
            CreditCardVendor.JCB              | "JCB"              | 16           | 3         | new CreditCardValidator([new CreditCardRange("3528", "3589", 16, 16)] as CreditCardRange[])
    }

    def "should pick every vendor at random"() {
        when:
            Set<String> vendors = (1..200).collect { creditCardProvider.get().vendor } as Set
        then:
            vendors == CreditCardVendor.values()*.displayName as Set
    }

    def "should return card expiry date"() {
        given:
            dateProducer.randomDateBetweenNowAndFuturePeriod(_) >> EXPIRY_DATE
        when:
            CreditCard creditCard = creditCardProvider.get()
        then:
            creditCard.expiryDate == EXPIRY_DATE
    }

    def "should return card expiry date string"() {
        given:
            dateProducer.randomDateBetweenNowAndFuturePeriod(_) >> EXPIRY_DATE
        when:
            CreditCard creditCard = creditCardProvider.get()
        then:
            creditCard.expiryDateAsString == "02/09"
    }

}
