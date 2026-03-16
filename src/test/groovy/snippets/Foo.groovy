package snippets

import com.devskiller.jfairy.Fairy
import com.devskiller.jfairy.producer.person.Person

class Foo {

	static void main(String[] args) {
		Person person = Fairy.create(Locale.default).person()

		println person.address
		println person.address.postalCode
	}

}
