package com.devskiller.jfairy

import java.lang.reflect.Method
import java.lang.reflect.Modifier

import spock.lang.Specification

import com.devskiller.jfairy.producer.util.LanguageCode

/**
 * Generates a sample of objects for every bundled locale, calls each of their getters reflectively (descending into
 * nested jFairy objects) and checks that no getter fails, returns null or an empty string, or always returns the same
 * value. Catches locales with missing or broken data and generators stuck on a single value.
 */
class GeneratedValuesVarySpec extends Specification {

    private static final int SEED = 42
    private static final int SAMPLES = 50

    private static final List<String> LANGUAGES = LanguageCode.values()*.name()*.toLowerCase(Locale.ROOT)

    private static final Map<String, Closure> GENERATORS = [
        person    : { Fairy fairy -> fairy.person() },
        company   : { Fairy fairy -> fairy.company() },
        iban      : { Fairy fairy -> fairy.iban() },
        creditCard: { Fairy fairy -> fairy.creditCard() },
    ]

    // Fixed for a locale by design
    private static final Set<String> CONSTANT = [
        'creditCard.getVendor',
        'iban.getCountry',
        'person.getAddress.getApartmentMark',
        'person.getNationality',
    ] as Set

    // Legitimately null or empty for some samples
    private static final Set<String> OPTIONAL = [
        'iban.getNationalCheckDigit',
        'person.getAddress.getApartmentNumber',
        'person.getMiddleName',
        'person.getNationalIdentificationNumber',
    ] as Set

    // Null or empty for every sample of these locales, which do not support them
    private static final Map<String, Set<String>> UNSUPPORTED = [
        'iban.getNationalCheckDigit'            : ['br', 'de', 'en', 'ja', 'ka', 'pl', 'sk', 'zh'] as Set,
        'person.getNationalIdentificationNumber': ['br', 'de', 'en', 'es', 'fr', 'it', 'ja', 'ka', 'tr', 'zh'] as Set,
    ]

    def "every #generator getter varies for #language"() {
        given:
            Fairy fairy = Fairy.builder().withRandomSeed(SEED).withLocale(Locale.forLanguageTag(language)).build()
            Map<String, Set<Object>> valuesByPath = [:].withDefault { [] as Set }
            List<String> problems = []

        when:
            SAMPLES.times {
                collect(GENERATORS[generator](fairy), generator, valuesByPath, problems)
            }
            valuesByPath.each { path, values ->
                boolean unsupported = language in UNSUPPORTED.get(path, [] as Set) && values.every { it == null || it == '' }
                if (values.size() < 2 && !(path in CONSTANT) && !unsupported) {
                    problems << "${path}: always ${values}".toString()
                }
            }

        then:
            problems.unique() == []

        where:
            [language, generator] << [LANGUAGES, GENERATORS.keySet()].combinations()
    }

    private static void collect(Object target, String path, Map<String, Set<Object>> valuesByPath, List<String> problems) {
        getters(target.class).each { Method getter ->
            String getterPath = "${path}.${getter.name}"
            Object value
            try {
                value = getter.invoke(target)
            } catch (Exception ex) {
                problems << "${getterPath}: threw ${ex.cause ?: ex}".toString()
                return
            }
            if ((value == null || value == '') && !(getterPath in OPTIONAL)) {
                problems << "${getterPath}: returned ${value == null ? 'null' : 'an empty string'}".toString()
            } else if (value != null && isJFairyObject(value)) {
                collect(value, getterPath, valuesByPath, problems)
            } else {
                valuesByPath[getterPath] << value
            }
        }
    }

    private static List<Method> getters(Class<?> type) {
        type.methods.findAll {
            it.parameterCount == 0 && !Modifier.isStatic(it.modifiers) && isJFairyClass(it.declaringClass) &&
                (it.name.startsWith('get') || it.name.startsWith('is'))
        }.sort { it.name }
    }

    private static boolean isJFairyObject(Object value) {
        !(value instanceof Enum) && isJFairyClass(value.class)
    }

    private static boolean isJFairyClass(Class<?> type) {
        type.name.startsWith('com.devskiller.jfairy.')
    }
}
