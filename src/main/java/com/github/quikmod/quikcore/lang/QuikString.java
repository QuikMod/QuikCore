/*
 */
package com.github.quikmod.quikcore.lang;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RlonRyan
 */
public class QuikString {
	
	private final Map<String, String> translations;
	
	@SerializedName(value = "default", alternate = {"normal"})
	private final String normal;

	public QuikString() {
		this.translations = new HashMap<>();
		this.normal = "Testing 1, 2, 3.";
	}

	public QuikString(String value) {
		this.translations = new HashMap<>(0);
		this.normal = value;
	}

	@Override
	public String toString() {
		return this.translations.getOrDefault(QuikTranslator.getLocale(), normal);
	}
	
}
