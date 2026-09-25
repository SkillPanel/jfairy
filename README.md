# jFairy by Devskiller

[](https://github.com/SkillPanel/jfairy/actions/workflows/build.yml)
[](https://central.sonatype.com/artifact/com.devskiller/jfairy)
[](https://javadoc.io/doc/com.devskiller/jfairy)

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

System.out.println(person.getFirstName());
// Chloe
System.out.println(person.getLastName());
// Barker
System.out.println(person.getEmail());
// barker@yahoo.com
System.out.println(person.getTelephoneNumber());
// 690-950-802
System.out.println(person.getJobTitle());
// Software Developer

Person adultMale = fairy.person(PersonProperties.male(), PersonProperties.minAge(21));
System.out.println(adultMale.isMale());
// true
System.out.println(adultMale.getDateOfBirth());
// at least 21 years earlier
```

### Creating related objects

```java
Fairy fairy = Fairy.create();
Company company = fairy.company();
System.out.println(company.getName());
// Robuten Associates
System.out.println(company.getUrl());
// http://www.robuteniaassociates.com

Person salesman = fairy.person(PersonProperties.withCompany(company));
System.out.println(salesman.getFullName());
// Juan Camacho
System.out.println(salesman.getCompanyEmail());
// juan.camacho@robuteniaassociates.com
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
| Brazilian Portuguese |     `br`     |
| Slovak               |     `sk`     |
| Turkish              |     `tr`     |
| Japanese             |     `ja`     |

```java
Fairy enFairy = Fairy.create();
// Locale.ENGLISH is default
Fairy plFairy = Fairy.create(Locale.forLanguageTag("pl"));
// Polish version
Fairy brFairy = Fairy.create(Locale.forLanguageTag("br"));
// Brazilian version
```

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

Invalid national identification and VAT numbers are implemented for the Polish, Slovak and
Swedish locales; other locales throw `UnsupportedOperationException`. IBANs work for every
country that has them.

## Thread safety

`Fairy` objects are not designed for concurrent use by multiple threads.
It is recommended to create a separate `Fairy` instance for each thread.

While some methods might appear safe, the underlying `Random` implementation can lead to contention and poor performance. Creating dedicated instances ensures both thread safety and optimal execution speed.

## JUnit 5 Extension

If you use JUnit 5, check out [jfairy-junit-extension](https://github.com/SkillPanel/jfairy-junit-extension) — an extension that allows injecting jFairy-generated objects directly into test method parameters.

## Building

This project uses Maven and can be built using the provided wrapper:

```bash
./mvnw
```

