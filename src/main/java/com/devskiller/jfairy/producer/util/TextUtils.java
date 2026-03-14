/*
 * Copyright (c) 2013. Codearte
 */
package com.devskiller.jfairy.producer.util;

import java.util.List;

public final class TextUtils {

	private TextUtils() {
	}

	public static String joinWithSpace(List<String> result) {
		return String.join(" ", result);
	}

	public static String stripAccents(String s) {
		// Replace polish character ł since bug https://issues.apache.org/jira/browse/LANG-1120
		return org.apache.commons.lang3.StringUtils.stripAccents(s).replaceAll("ł", "l").replaceAll("Ł", "L");
	}

	public static String stripSharpS(String s) {
		return s.replace("\u00DF", "ss");
	}

	public static String escapeNonAscii(String input) {
		if (input == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c > 0x7f) {
				sb.append(String.format("\\u%04x", (int) c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
