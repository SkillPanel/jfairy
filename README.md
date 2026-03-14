# jFairy by Devskiller

[![Build](https://github.com/SkillPanel/jfairy/actions/workflows/build.yml/badge.svg)](https://github.com/SkillPanel/jfairy/actions/workflows/build.yml) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.devskiller/jfairy/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.devskiller/jfairy) [![Javadocs](http://www.javadoc.io/badge/com.devskiller/jfairy.svg)](http://www.javadoc.io/doc/com.devskiller/jfairy)

Java fake data generator. Based on Wikipedia:

> Fairyland, in folklore, is the fabulous land or abode of fairies or fays.

## Try jFairy online!

https://devskiller.com/datafairy/

## Usage

Creating simple objects:

```java
Fairy fairy = Fairy.create();
Person person = fairy.person();

System.out.println(person.getFirstName());            
// Chloe Barker
System.out.println(person.getEmail());               
// barker@yahoo.com
System.out.println(person.getTelephoneNumber());     
// 690-950-802

Person adultMale = fairy.person(PersonProperties.male(), PersonProperties.minAge(21));
System.out.println(adultMale.isMale());           
// true
System.out.println(adultMale.getDateOfBirth());      
// at least 21 years earlier
```

Creating related objects:

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

Locale support:

```java
Fairy enFairy = Fairy.create();                               
// Locale.ENGLISH is default
Fairy plFairy = Fairy.create(Locale.forLanguageTag("pl"));    
// Polish version
```

## Thread safety

`Fairy` object should not be used concurrently by multiple threads. It is recommended to create `Fairy` instance for each thread.

Some methods are not thread-safe when multiple threads share the same `Fairy` object. For other methods it is still recommended to create a separate `Fairy` object, because `Random` object utilized underneath does not perform well when used concurrently by multiple threads.

## Other samples

Look into [code samples](https://github.com/Devskiller/jfairy/tree/master/src/test/groovy/snippets/)

## Building

This project can be built using maven command:

    ./mvnw install

