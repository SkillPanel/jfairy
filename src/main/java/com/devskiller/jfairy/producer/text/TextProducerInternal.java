/*
 * Copyright (c) 2013. Codearte
 */
package com.devskiller.jfairy.producer.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;

import static com.devskiller.jfairy.producer.util.StringUtils.capitalize;
import static com.devskiller.jfairy.producer.util.StringUtils.joinWithSpace;
import static com.devskiller.jfairy.producer.util.StringUtils.replaceChars;
import static com.devskiller.jfairy.producer.util.StringUtils.split;
import static com.devskiller.jfairy.producer.util.StringUtils.uncapitalize;

/**
 * Internal component for generating and formatting random text, sentences, and words.
 * <p>
 * This class handles the low-level string manipulations for both standard and
 * Latin-based (Lorem Ipsum) text generation.
 *
 * @author jkubrynski@gmail.com
 * @since 2013-11-16
 */
public class TextProducerInternal {

    private static final String LOREM_IPSUM = "loremIpsum";

    private static final String TEXT = "text";

    private static final String ALPHABET = "alphabet";

    private static final int WORD_COUNT_PRECISION_IN_SENTENCE = 6;

    private final BaseProducer baseProducer;

    private final String loremIpsum;

    private final String text;

    private final List<String> words;

    private final String alphabet;

    private final int maxAlphabetIndex;

    private final List<String> latinWords;

    public TextProducerInternal(DataMaster dataMaster, BaseProducer baseProducer) {
        this.baseProducer = baseProducer;
        loremIpsum = dataMaster.getString(LOREM_IPSUM);
        text = dataMaster.getString(TEXT);
        words = new ArrayList<>(Arrays.asList(split(text, ' ')));
        latinWords = new ArrayList<>(Arrays.asList(split(loremIpsum, ' ')));
        alphabet = dataMaster.getString(ALPHABET);
        maxAlphabetIndex = alphabet.length() - 1;
    }

    /**
     * Returns the raw Lorem Ipsum template string.
     *
     * @return the full lorem ipsum text
     */
    public String loremIpsum() {
        return loremIpsum;
    }

    /**
     * Generates a space-separated string of random words.
     *
     * @param words source list of words
     * @param count base number of words
     * @param precision maximum additional random words to add
     * @return a string containing the random words
     */
    public String rawWords(List<String> words, int count, int precision) {
        List<String> result = readRawWords(words, count, precision);
        return joinWithSpace(result);
    }

    /**
     * Returns a list of cleaned Latin words without punctuation.
     *
     * @param count number of words to return
     * @return a space-separated string of lowercase Latin words
     */
    public String cleanLatinWords(int count) {
        return cleanWords(latinWords, count);
    }

    /**
     * Returns a list of cleaned standard words without punctuation.
     *
     * @param count number of words to return
     * @return a space-separated string of lowercase words
     */
    public String cleanWords(int count) {
        return cleanWords(words, count);
    }

    private String cleanWords(List<String> words, int count) {
        List<String> result = new ArrayList<>();
        for (String part : readRawWords(words, count, 0)) {
            // efficiently remove punctuation and lowercase
            result.add(uncapitalize(replaceChars(part, "., ", "")));
        }
        return joinWithSpace(result);
    }

    /**
     * Generates a random string of a specific length using the configured alphabet.
     *
     * @param charsCount the desired length of the string
     * @return a random string
     */
    public String randomString(int charsCount) {
        StringBuilder sb = new StringBuilder(charsCount);
        for (int i = 0; i < charsCount; i++) {
            sb.append(alphabet.charAt(baseProducer.randomInt(maxAlphabetIndex)));
        }
        return sb.toString();
    }

    private List<String> readRawWords(List<String> words, int count, int precision) {
        return baseProducer.randomElements(words, baseProducer.randomBetween(count, count + precision));
    }

    /**
     * Returns the raw standard text template.
     *
     * @return the base text string
     */
    public String text() {
        return text;
    }

    /**
     * Generates a formatted sentence from standard words.
     *
     * @param wordCount number of words in the sentence
     * @return a formatted sentence
     */
    public String sentence(int wordCount) {
        return sentence(words, wordCount);
    }

    private String sentence(List<String> words, int wordCount) {
        String randomWords = rawWords(words, wordCount, WORD_COUNT_PRECISION_IN_SENTENCE);

        if (randomWords == null || randomWords.isEmpty()) {
            return ".";
        }

        // simplified logic: capitalize and ensure proper ending
        String result = capitalize(randomWords).trim();

        // clean up trailing commas from random selection
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }

        // enforce single trailing period
        if (!result.endsWith(".")) {
            result += ".";
        }

        return result;
    }

    /**
     * Generates a formatted sentence from Latin words.
     *
     * @param wordCount number of words in the sentence
     * @return a formatted Latin sentence
     */
    public String latinSentence(int wordCount) {
        return sentence(latinWords, wordCount);
    }
}
