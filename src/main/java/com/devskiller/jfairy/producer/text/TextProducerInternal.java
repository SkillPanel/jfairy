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
import static com.devskiller.jfairy.producer.util.StringUtils.endsWith;
import static com.devskiller.jfairy.producer.util.StringUtils.joinWithSpace;
import static com.devskiller.jfairy.producer.util.StringUtils.removeEnd;
import static com.devskiller.jfairy.producer.util.StringUtils.replaceChars;
import static com.devskiller.jfairy.producer.util.StringUtils.split;
import static com.devskiller.jfairy.producer.util.StringUtils.uncapitalize;

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

	public String loremIpsum() {
		return loremIpsum;
	}

	public String rawWords(List<String> words, int count, int precision) {
		List<String> result = readRawWords(words, count, precision);
		return joinWithSpace(result);
	}

	public String cleanLatinWords(int count) {
		return cleanWords(latinWords, count);
	}

	public String cleanWords(int count) {
		return cleanWords(words, count);
	}

	private String cleanWords(List<String> words, int count) {
		List<String> result = new ArrayList<>();
		for (String part : readRawWords(words, count, 0)) {
			result.add(uncapitalize(replaceChars(part, "., ", "")));
		}
		return joinWithSpace(result);
	}

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

	public String text() {
		return text;
	}

	public String sentence(int wordCount) {
		return sentence(words, wordCount);
	}

	public String latinSentence(int wordCount) {
		return sentence(latinWords, wordCount);
	}

	private String sentence(List<String> words, int wordCount) {
		String randomWords = rawWords(words, wordCount, WORD_COUNT_PRECISION_IN_SENTENCE);
		String[] parts = randomWords.split("\\. ");
		List<String> sentences = new ArrayList<>(parts.length);
		for (String sentence : parts) {
			sentences.add(capitalize(sentence));
		}
		String sentence = capitalize(String.join(". ", sentences));
		sentence = removeEnd(sentence, ",");
		if (!endsWith(sentence, ".")) {
			sentence += ".";
		}
		return sentence;
	}
}
