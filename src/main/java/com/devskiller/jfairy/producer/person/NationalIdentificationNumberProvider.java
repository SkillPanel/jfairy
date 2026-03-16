package com.devskiller.jfairy.producer.person;

import java.time.LocalDate;
import java.util.function.Supplier;

public interface NationalIdentificationNumberProvider extends Supplier<NationalIdentificationNumber> {

	NationalIdentificationNumber get();

	void setIssueDate(LocalDate dateOfBirth);

	void setSex(Person.Sex sex);
}
