# jFairy by Devskiller

[![Build](https://github.com/SkillPanel/jfairy/actions/workflows/build.yml/badge.svg)](https://github.com/SkillPanel/jfairy/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.devskiller/jfairy)](https://central.sonatype.com/artifact/com.devskiller/jfairy)
[![Javadoc](https://javadoc.io/badge2/com.devskiller/jfairy/javadoc.svg)](https://javadoc.io/doc/com.devskiller/jfairy)
[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://openjdk.org/projects/jdk/17/)
[![License](https://img.shields.io/github/license/SkillPanel/jfairy)](LICENSE)

Java fake data generator. According to Wikipedia:

> Fairyland, in folklore, is the fabulous land or abode of fairies or fays.

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.devskiller</groupId>
    <artifactId>jfairy</artifactId>
    <version>0.9.0</version>
</dependency>
```

## Usage

### Creating simple objects

```java
Fairy fairy = Fairy.create();
Person person = fairy.person();

String name = person.getFullName();          // Camila Rosa
String email = person.getEmail();            // camila.rosa@yahoo.com
String phone = person.getTelephoneNumber();  // 786-828-8553
String city = person.getAddress().getCity(); // Miami
String job = person.getJobTitle();           // DevOps Engineer
```

Properties narrow down what gets generated:

```java
Person adult = fairy.person(PersonProperties.male(), PersonProperties.minAge(21));

boolean male = adult.isMale(); // true
int age = adult.getAge();      // 21 or older
```

### Creating related objects

```java
Fairy fairy = Fairy.create(Locale.GERMAN);
Company company = fairy.company();
String companyName = company.getName(); // Kaplooie Sprengtechnik AG
String url = company.getUrl();          // http://www.kaplooiesprengtechnik.net

Person employee = fairy.person(PersonProperties.withCompany(company));
String name = employee.getFullName();             // Blanka Hohn
String companyEmail = employee.getCompanyEmail(); // blanka.hohn@kaplooiesprengtechnik.net
```

## Supported locales

| Locale               | Language tag |
|:---------------------|:------------:|
| English (default)    |     `en`     |
| Polish               |     `pl`     |
| German               |     `de`     |
| French               |     `fr`     |
| Spanish              |     `es`     |
| Swedish              |     `sv`     |
| Chinese              |     `zh`     |
| Georgian             |     `ka`     |
| Italian              |     `it`     |
| Brazilian Portuguese |     `pt`     |
| Slovak               |     `sk`     |
| Turkish              |     `tr`     |
| Japanese             |     `ja`     |

```java
Fairy enFairy = Fairy.create();
// Locale.ENGLISH is default
Fairy plFairy = Fairy.create(Locale.forLanguageTag("pl"));
// Polish version
Fairy brFairy = Fairy.create(Locale.forLanguageTag("pt-BR"));
// Brazilian version
```

Locales are matched by language only, so `de-AT` gets the German data and any Portuguese locale the
Brazilian data. The former tag `br` (actually the code for Breton) is deprecated and logs a warning.

## Custom data

The bundled data is maintained as YAML in the repository and ships in the jar as `.properties` files
(`jfairy.properties` plus `jfairy_<language>.properties`). To add or replace data, put a file with the same name
on your classpath. Every key it defines replaces the bundled one, and keys it leaves out keep the bundled data:

```properties
# src/test/resources/jfairy_en.properties
cities=Springfield, Shelbyville
firstNames.male=Homer,Bart
```

Lists are comma-separated; spaces around elements are trimmed and empty elements skipped. Data split by sex uses
`.male` / `.female` sub-keys: overriding one (as above) keeps the bundled female names, while a plain key
(`lastNames=Simpson`) replaces the list for every sex. Files are read as UTF-8.

A list element may carry a weight to be picked more often: with `lastNames=Nowak*98387,Kowalski*66589,Adamiec`,
Nowak comes up about 98387 times as often as Adamiec, since an element without a weight counts as 1. Weights are
positive whole numbers, relative within one list, and a list without any weight is picked uniformly. A malformed
weight fails when the key is first used, naming the file and the key.

With `Fairy.builder().withFilePrefix("mydata")` jFairy reads `mydata.properties` and `mydata_<language>.properties` instead.

> **Upgrading from 0.9.x:** custom `.yml` data files are no longer read. Convert them to `.properties` as shown above.

## Unique values

```java
UniqueFairy unique = fairy.unique();
Person p1 = unique.person();  // unique by email
Person p2 = unique.person();  // different email than p1
Company c = unique.company(); // unique by name
```

For custom uniqueness keys:

```java
UniqueEnforcer<Person> unique = UniqueEnforcer.of(fairy::person, Person::getFullName);
Person p = unique.next();
```

## Invalid identifiers

For negative tests of your validators, `fairy.invalid()` generates identifiers that keep a valid
format but fail checksum validation:

```java
Fairy fairy = Fairy.create(Locale.forLanguageTag("pl"));
InvalidFairy invalid = fairy.invalid();

String pesel = invalid.nationalIdentificationNumber(); // wrong check digit
String nip = invalid.vatIdentificationNumber();        // wrong check digit
IBAN iban = invalid.iban();                            // wrong check digits
```

To get a person with an invalid PESEL, pass the same birth date and sex to the number and to
the person, so that only the checksum is wrong:

```java
LocalDate birthDate = LocalDate.of(1990, 5, 17);
String pesel = invalid.nationalIdentificationNumber(
    NationalIdentificationNumberProperties.dateOfBirth(birthDate),
    NationalIdentificationNumberProperties.sex(Person.Sex.FEMALE));

Person person = fairy.person(
    PersonProperties.female(),
    PersonProperties.withDateOfBirth(birthDate),
    PersonProperties.withNationalIdentificationNumber(pesel));
```

The Italian tax code (Codice Fiscale) is derived from the holder's name as well, so pass
`NationalIdentificationNumberProperties.firstName(...)` and `lastName(...)` too and use the same
names for the person.

Invalid national identification and VAT numbers are implemented for the Italian, Polish, Slovak
and Swedish locales; other locales throw `UnsupportedOperationException`. IBANs work for every
country that has them.

## Thread safety

`Fairy` objects are not designed for concurrent use by multiple threads.
It is recommended to create a separate `Fairy` instance for each thread.

While some methods might appear safe, the underlying `Random` implementation can lead to contention and poor performance. Creating dedicated instances ensures both thread safety and optimal execution speed.

## Building

This project uses Maven and can be built using the provided wrapper:

```bash
./mvnw
```

