package com.devskiller.jfairy.producer.payment

import java.time.LocalDateTime

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
        dataMaster.readResources("jfairy.yml")
        creditCardProvider = new CreditCardProvider(dataMaster, baseProducer, dateProducer)
    }

    def "should return credit card basic data"() {
        when:
            CreditCard creditCard = creditCardProvider.get()
        then:
            creditCard.vendor == 'Visa'
            creditCard.cardNumber.length() == 16
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
