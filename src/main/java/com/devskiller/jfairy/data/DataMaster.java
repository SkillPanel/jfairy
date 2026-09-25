package com.devskiller.jfairy.data;

import java.util.List;

import com.devskiller.jfairy.producer.util.LanguageCode;

/**
 * Providing access to localized data used by producers.
 * <p>
 * This master component acts as the central registry for retrieving
 * strings and structured data based on specific keys and language codes.
 *
 * @author Olga Maciaszek-Sharma
 * @since 23.04.15
 */
public interface DataMaster {

    /**
     * Returns a single string value associated with the given key.
     *
     * @param key the unique identifier for the data entry
     * @return the string value found for the key
     */
    String getString(String key);

    /**
     * Returns the values of the list associated with the given key, without their weights. Picking from this list
     * yourself is uniform; use {@link #getRandomValue(String)} for a pick that honours weights.
     *
     * @param key the unique identifier for the data entries
     * @return a list of string values found for the key
     */
    List<String> getStringList(String key);

    /**
     * Retrieves structured data of a specific type and converts it to the requested class.
     * <p>
     * The entry under {@code dataKey} may either be a map keyed by {@code type} (e.g. {@code male}/{@code female}
     * name lists) or a plain list. In the latter case {@code type} is ignored and a random element of that list
     * is returned; this lets a locale share a single, non-gendered list (e.g. surnames that don't vary by sex)
     * instead of duplicating the same entries under every type.
     *
     * @param dataKey the root key for the data search
     * @param type the specific sub-type or category
     * @param resultClass the class type to which the result should be cast
     * @param <T> the type of the result object
     * @return an instance of the requested type containing the values
     */
    <T> T getValuesOfType(String dataKey, String type, Class<T> resultClass);

    /**
     * Selects a random value from the entries associated with the given key, honouring their weights if the list has
     * any.
     *
     * @param key the unique identifier for the data list
     * @return a randomly selected string value
     */
    String getRandomValue(String key);

    /**
     * Returns the language code currently used by this data master.
     *
     * @return the active LanguageCode
     */
    LanguageCode getLanguage();

}
