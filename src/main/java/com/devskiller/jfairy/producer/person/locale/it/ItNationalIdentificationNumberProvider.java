package com.devskiller.jfairy.producer.person.locale.it;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumber;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProvider;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.util.CheckDigits;

import static com.devskiller.jfairy.producer.person.PersonProvider.FIRST_NAME;
import static com.devskiller.jfairy.producer.person.PersonProvider.LAST_NAME;
import static com.devskiller.jfairy.producer.util.StringUtils.latinize;

/**
 * Italian tax code of a natural person (Codice Fiscale).
 * <p>
 * <a href="https://en.wikipedia.org/wiki/Italian_fiscal_code">Italian fiscal code</a>
 * <p>
 * E.g. MRTMTT25D09F205Z: three letters each from the last and the first name, the year of birth, a letter
 * for the month, the day of birth (plus 40 for women), the cadastral code of the birthplace and a check letter.
 * Numbers with letters substituted for digits (omocodia, used to resolve collisions) are not generated.
 *
 * @since 0.10.0
 */
public class ItNationalIdentificationNumberProvider implements NationalIdentificationNumberProvider {

    private static final Pattern FORMAT            = Pattern.compile("^[A-Z]{6}\\d{2}[ABCDEHLMPRST]\\d{2}[A-Z]\\d{3}[A-Z]$");
    private static final String  MONTH_CODES       = "ABCDEHLMPRST";
    private static final String  VOWELS            = "AEIOU";
    private static final String  PADDING           = "XXX";
    private static final int     NAME_CODE_LENGTH  = 3;
    private static final int     FEMALE_DAY_OFFSET = 40;
    private static final int     MAX_AGE_IN_YEARS  = 100;

    /** Cadastral (Belfiore) codes of the birthplace, here some of the larger municipalities. */
    private static final List<String> BIRTHPLACE_CODES = List.of(
        "A662", // Bari
        "A944", // Bologna
        "B157", // Brescia
        "C351", // Catania
        "D612", // Firenze
        "D969", // Genova
        "F205", // Milano
        "F839", // Napoli
        "G224", // Padova
        "G273", // Palermo
        "H501", // Roma
        "L219", // Torino
        "L424", // Trieste
        "L736", // Venezia
        "L781"  // Verona
    );

    /** Values of characters in odd positions (first, third, ...), indexed by digit or by letter. */
    private static final int[] ODD_POSITION_VALUES = {
        1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23
    };

    private final DataMaster   dataMaster;
    private final DateProducer dateProducer;
    private final BaseProducer baseProducer;

    private LocalDate  issueDate;
    private Person.Sex sex;
    private String     firstName;
    private String     lastName;

    public ItNationalIdentificationNumberProvider(DataMaster dataMaster, DateProducer dateProducer, BaseProducer baseProducer,
                                                  NationalIdentificationNumberProperties.Property... properties) {
        this.dataMaster = dataMaster;
        this.dateProducer = dateProducer;
        this.baseProducer = baseProducer;

        for (NationalIdentificationNumberProperties.Property property : properties) {
            property.apply(this);
        }
    }

    /**
     * Generates a tax code, picking random values for any property that was not set.
     *
     * @return a tax code with a valid check letter
     */
    @Override
    public NationalIdentificationNumber get() {
        if (issueDate == null) {
            issueDate = dateProducer.randomDateInThePast(MAX_AGE_IN_YEARS).toLocalDate();
        }
        if (sex == null) {
            sex = baseProducer.trueOrFalse() ? Person.Sex.MALE : Person.Sex.FEMALE;
        }
        if (firstName == null) {
            firstName = dataMaster.getValuesOfType(FIRST_NAME, sex.name(), String.class);
        }
        if (lastName == null) {
            lastName = dataMaster.getValuesOfType(LAST_NAME, sex.name(), String.class);
        }

        String code = String.format("%s%s%02d%c%02d%s",
            lastNameCode(lastName),
            firstNameCode(firstName),
            issueDate.getYear() % 100,
            MONTH_CODES.charAt(issueDate.getMonthValue() - 1),
            issueDate.getDayOfMonth() + (sex == Person.Sex.FEMALE ? FEMALE_DAY_OFFSET : 0),
            baseProducer.randomElement(BIRTHPLACE_CODES));
        return new NationalIdentificationNumber(code + checkLetter(code));
    }

    /**
     * Generates a tax code with a wrong check letter.
     *
     * @return a tax code that fails checksum validation
     */
    @Override
    public NationalIdentificationNumber getInvalid() {
        return new NationalIdentificationNumber(CheckDigits.replaceLastLetter(get().getValue(), baseProducer));
    }

    /** Sets the date of birth encoded in the tax code. */
    @Override
    public void setIssueDate(LocalDate dateOfBirth) {
        this.issueDate = dateOfBirth;
    }

    /** Sets the sex, which shifts the encoded day of birth for women. */
    @Override
    public void setSex(Person.Sex sex) {
        this.sex = sex;
    }

    /** Sets the given names the tax code is derived from, separated by spaces if there are several. */
    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** Sets the last name the tax code is derived from. */
    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Checks the format and the check letter of a tax code.
     *
     * @param taxCode the tax code to check
     * @return {@code true} if the tax code is well-formed and its check letter matches
     */
    public static boolean isValid(String taxCode) {
        return FORMAT.matcher(taxCode).matches()
            && taxCode.charAt(taxCode.length() - 1) == checkLetter(taxCode.substring(0, taxCode.length() - 1));
    }

    /**
     * Encodes the last name: its consonants, then its vowels, padded with X.
     *
     * @param name the last name
     * @return three upper case letters
     */
    static String lastNameCode(String name) {
        String letters = letters(name);
        return (consonants(letters) + vowels(letters) + PADDING).substring(0, NAME_CODE_LENGTH);
    }

    /**
     * Encodes the given names like the last name, except that the first, third and fourth consonant are used
     * if there are more than three. Several given names are encoded together.
     *
     * @param name the given names
     * @return three upper case letters
     */
    static String firstNameCode(String name) {
        String letters = letters(name);
        String consonants = consonants(letters);
        if (consonants.length() > NAME_CODE_LENGTH) {
            return "" + consonants.charAt(0) + consonants.charAt(2) + consonants.charAt(3);
        }
        return (consonants + vowels(letters) + PADDING).substring(0, NAME_CODE_LENGTH);
    }

    /**
     * Computes the check letter from the first 15 characters of a tax code.
     *
     * @param code the tax code without its check letter
     * @return the check letter
     */
    private static char checkLetter(String code) {
        int sum = 0;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            int index = Character.isDigit(c) ? c - '0' : c - 'A';
            // positions are counted from one, so even indexes are odd positions
            sum += i % 2 == 0 ? ODD_POSITION_VALUES[index] : index;
        }
        return (char) ('A' + sum % CheckDigits.LETTERS);
    }

    /**
     * Reduces a name to its upper case Latin letters, dropping accents, spaces and apostrophes.
     *
     * @param name the name
     * @return the letters A to Z of the name
     */
    private static String letters(String name) {
        return latinize(name).toUpperCase(Locale.ROOT).replaceAll("[^A-Z]", "");
    }

    /**
     * Returns the consonants of a string of upper case letters.
     *
     * @param letters upper case letters
     * @return the consonants in their original order
     */
    private static String consonants(String letters) {
        return letters.replaceAll("[" + VOWELS + "]", "");
    }

    /**
     * Returns the vowels of a string of upper case letters.
     *
     * @param letters upper case letters
     * @return the vowels in their original order
     */
    private static String vowels(String letters) {
        return letters.replaceAll("[^" + VOWELS + "]", "");
    }
}
