package com.devskiller.jfairy.data

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import spock.lang.Specification
import spock.lang.TempDir

/**
 * Runs the build-time converter build/yaml2properties.groovy against temporary YAML files.
 */
class Yaml2PropertiesScriptSpec extends Specification {

    private static final Path SCRIPT = Paths.get('build', 'yaml2properties.groovy')

    @TempDir
    Path sourceDir

    @TempDir
    Path outputDir

    def "flattens scalars, lists and one-level maps"() {
        when:
            Properties properties = convert('jfairy_xx.yml', '''\
language: 'PL'
cities: [Kraków, Łódź]
firstNames: {male: [Adam], female: [Anna, Ewa]}
creditCardPrefixes: {Visa: [4]}
''')

        then:
            properties == [
                'language'               : 'PL',
                'cities'                 : 'Kraków,Łódź',
                'firstNames.male'        : 'Adam',
                'firstNames.female'      : 'Anna,Ewa',
                'creditCardPrefixes.Visa': '4',
            ]
    }

    def "escapes special characters and keeps multi-line scalars"() {
        when:
            Properties properties = convert('jfairy_xx.yml', '''\
text: |
  a=b: c#d\\e
  zażółć
''')

        then:
            properties.getProperty('text') == 'a=b: c#d\\e\nzażółć\n'
    }

    def "writes sorted keys without a timestamp comment"() {
        when:
            convert('jfairy_xx.yml', '''\
zeta: [z]
alpha: [a]
middle: m
''')
            List<String> lines = Files.readAllLines(outputDir.resolve('jfairy_xx.properties'), StandardCharsets.UTF_8)

        then:
            lines == ['alpha=a', 'middle=m', 'zeta=z']
    }

    def "fails on #problem"() {
        when:
            convert('jfairy_xx.yml', yaml)

        then:
            Exception ex = thrown()
            ex.message.contains(expectedMessagePart)

        where:
            problem                           | yaml                                  | expectedMessagePart
            'a mapping inside a list'         | 'companyNames: [ACME, Foo; bar: VÚB]' | 'companyNames'
            'a comma inside an element'       | "cities: ['A, B']"                    | "','"
            'an empty list'                   | 'cities: []'                          | 'must not be empty'
            'an empty element'                | "cities: [A, '']"                     | 'non-empty'
            'nesting deeper than one map'     | 'firstNames: {male: {x: [A]}}'        | 'firstNames.male'
            'a dot in a key'                  | 'a.b: [x]'                            | "'.'"
            'a duplicate key'                 | 'cities: [A]\ncities: [B]'            | 'cities'
            'keys differing only in case'     | 'Cities: [A]\ncities: [B]'            | "key 'cities': differs from 'Cities' only in case"
            'sub-keys differing only in case' | 'cc: {Visa: [4], VISA: [5]}'          | "key 'cc.VISA': differs from 'cc.Visa' only in case"
    }

    private Properties convert(String fileName, String yaml) {
        Files.writeString(sourceDir.resolve(fileName), yaml, StandardCharsets.UTF_8)
        Binding binding = new Binding(sourceDir: sourceDir.toString(), outputDir: outputDir.toString())
        new GroovyShell(binding).evaluate(SCRIPT.toFile())
        Properties properties = new Properties()
        Files.newBufferedReader(outputDir.resolve(fileName.replace('.yml', '.properties')), StandardCharsets.UTF_8).withCloseable {
            properties.load(it)
        }
        properties
    }
}
