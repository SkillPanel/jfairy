package com.devskiller.jfairy.producer.person;
import java.util.function.Supplier;

import java.time.LocalDate;


public interface NationalIdentificationNumberProvider extends Supplier<NationalIdentificationNumber> {

	NationalIdentificationNumber get();

	void setIssueDate(LocalDate dateOfBirth);

	void setSex(Person.Sex sex);
}
