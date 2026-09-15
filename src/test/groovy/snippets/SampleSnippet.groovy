package snippets

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.company.Company
import com.devskiller.jfairy.producer.person.Person
import com.devskiller.jfairy.producer.person.PersonProperties

println "\n * With context"
Fairy fairy = Fairy.create(Locale.forLanguageTag("en"))
3.times {
    Person person = fairy.person()

    println "${person.fullName} <${person.email}>"

}

// Print word
println fairy.textProducer().word(3)
println fairy.textProducer().sentence(5)

println fairy.person().firstName
println fairy.person().firstName

Person person = fairy.person()
println ""
println "         First name: " + person.firstName
println "        Middle name: " + person.middleName
println "          Last name: " + person.lastName
println "           Username: " + person.username
println "           Password: " + person.password
println "             isMale: " + person.male
println "           isFemale: " + person.female
println "              Phone: " + person.telephoneNumber
println "      Date of birth: " + person.dateOfBirth
println "                Age: " + person.age
println "     National Id No: " + person.nationalIdentificationNumber
println "National Id Card No: " + person.nationalIdentityCardNumber
println "        Passport No: " + person.passportNumber
println "        Postal Code: " + person.address.postalCode
println "               City: " + person.address.city
println "             Street: " + person.address.street
println "      Street number: " + person.address.streetNumber
println "       Full address: " + person.address.toString()

println ""

3.times {
    println "  Men: " + fairy.person(PersonProperties.male()).fullName
}

3.times {
    println "Women: " + fairy.person(PersonProperties.female()).fullName
}

println fairy.person().nationalIdentityCardNumber
println fairy.person().nationalIdentificationNumber

Company company = fairy.company()
println "Company: " + company.name
println "URL: " + company.url
println "VAT: " + company.vatIdentificationNumber
company = fairy.company()
println "Company: " + company.name
println "URL: " + company.url
println "VAT: " + company.vatIdentificationNumber
