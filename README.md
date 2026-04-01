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
    <version>0.8.2</version>
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

```java
Fairy enFairy = Fairy.create();
// Locale.ENGLISH is default
Fairy plFairy = Fairy.create(Locale.forLanguageTag("pl"));
// Polish version
Fairy brFairy = Fairy.create(Locale.forLanguageTag("br"));
// Brazilian version
```

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

