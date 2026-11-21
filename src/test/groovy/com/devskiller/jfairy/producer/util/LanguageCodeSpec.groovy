package com.devskiller.jfairy.producer.util

import com.devskiller.jfairy.producer.person.Country

import spock.lang.Specification

class LanguageCodeSpec extends Specification {

    def "should resolve language tag #tag to #expected"() {
        expect:
            LanguageCode.forLocale(Locale.forLanguageTag(tag)) == Optional.ofNullable(expected)
        where:
            tag     | expected
            "en"    | LanguageCode.EN
            "de-AT" | LanguageCode.DE
            "pt"    | LanguageCode.BR
            "pt-BR" | LanguageCode.BR
            "pt-PT" | LanguageCode.BR
            "br"    | LanguageCode.BR
            "nl"    | null
    }

    def "should use ISO 639 language in primary locale of #country"() {
        expect:
            country.primaryLocale.toLanguageTag() == expected
        where:
            country         | expected
            Country.Brazil  | "pt-BR"
            Country.Germany | "de-DE"
    }
}
