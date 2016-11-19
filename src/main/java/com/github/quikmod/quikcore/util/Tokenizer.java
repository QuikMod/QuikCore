/*
 */
package com.github.quikmod.quikcore.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ryan
 */
public final class Tokenizer {
	
	public static final Pattern TOKENIZER = Pattern.compile("\"([^\"]*)\"|(\\S+)");
	
	public static Deque<String> tokenize(String input) {
		Matcher m = TOKENIZER.matcher(input);
		Deque<String> tokens = new ArrayDeque<>();
		while (m.find()) {
			tokens.add(m.group());
		}
		return tokens;
	}
	
}
