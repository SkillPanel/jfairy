package com.devskiller.jfairy.data

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import spock.lang.Specification
import spock.lang.TempDir

/**
 * Runs build/pesel2yaml.groovy against small CSV fixtures, without network access.
 */
class Pesel2YamlScriptSpec extends Specification {

    private static final Path SCRIPT = Paths.get('build', 'pesel2yaml.groovy')

    private static final String YAML = '''\
alphabet: abc
firstNames: {
  male: [Adam],
  female: [Anna]
}
lastNames: {
  male: [Adamiec],
  female: [Adamiec]
}

cities: [Kraków]
'''

    private static final String EXPECTED = '''\
alphabet: abc
# PESEL registry (dane.gov.pl, CC0), state as of 2026-01-20; regenerate with ./mvnw -Ppesel initialize
firstNames:
    male:
    - Piotr*682516
    - Krzysztof*631898
    female:
    - Maria*2000
    - Anna*1000

lastNames:
    male:
    - Nowak*98387
    - Kowalski*66589
    female:
    - Nowak-Kowalska*10
    - O'Brien*5

cities: [Kraków]
'''

    @TempDir
    Path dir

    def "replaces the name blocks with the most frequent names, title-cased and weighted"() {
        given:
            Path yaml = write('jfairy_pl.yml', YAML)

        when:
            run(yaml)

        then:
            Files.readString(yaml, StandardCharsets.UTF_8) == EXPECTED
    }

    def "gives the same file when run again"() {
        given:
            Path yaml = write('jfairy_pl.yml', YAML)

        when:
            run(yaml)
            run(yaml)

        then:
            Files.readString(yaml, StandardCharsets.UTF_8) == EXPECTED
    }

    def "fails instead of writing an empty block when a source has no usable rows"() {
        given:
            Path yaml = write('jfairy_pl.yml', YAML)

        when:
            run(yaml, [lastNamesFemale: csv('<html>Not found</html>\r\n')])

        then:
            Exception ex = thrown()
            ex.message.contains('no names')
            Files.readString(yaml, StandardCharsets.UTF_8) == YAML
    }

    def "fails when the file has no name blocks"() {
        given:
            Path yaml = write('jfairy_pl.yml', 'alphabet: abc\n')

        when:
            run(yaml)

        then:
            Exception ex = thrown()
            ex.message.contains('firstNames')
    }

    private void run(Path yaml, Map<String, String> overrides = [:]) {
        Map<String, Object> variables = [
            yamlFile        : yaml.toString(),
            dataDate        : '2026-01-20',
            firstNamesLimit : '2',
            lastNamesLimit  : '2',
            firstNamesMale  : csv('IMIĘ_PIERWSZE,PŁEĆ,LICZBA_WYSTĄPIEŃ\r\nPIOTR,MĘŻCZYZNA,682516\r\n'
                + 'BRAK DANYCH,MĘŻCZYZNA,900000\r\nKRZYSZTOF,MĘŻCZYZNA,631898\r\nTOMASZ,MĘŻCZYZNA,533217\r\n'),
            firstNamesFemale: csv('IMIĘ_PIERWSZE,PŁEĆ,LICZBA_WYSTĄPIEŃ\r\nANNA,KOBIETA,1000\r\nMARIA,KOBIETA,2000\r\n'),
            lastNamesMale   : csv('Nazwisko aktualne,Liczba\r\nKOWALSKI,66589\r\nNOWAK,98387\r\nWIŚNIEWSKI,53079\r\n'),
            lastNamesFemale : csv("Nazwisko aktualne,Liczba\r\nNOWAK-KOWALSKA,10\r\nO'BRIEN,5\r\nŻÓŁĆ1,7\r\n"),
        ]
        variables.putAll(overrides)
        new GroovyShell(new Binding(variables)).evaluate(SCRIPT.toFile())
    }

    private String csv(String content) {
        Path file = Files.createTempFile(dir, 'pesel', '.csv')
        Files.writeString(file, content, StandardCharsets.UTF_8)
        file.toUri().toString()
    }

    private Path write(String name, String content) {
        Files.writeString(dir.resolve(name), content, StandardCharsets.UTF_8)
    }
}
