/*
 */
package com.github.quikmod.quikcore.lang;

import com.github.quikmod.quikcore.defaults.QuikDefaultTranslator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author RlonRyan
 */
public final class QuikTranslator {

	private static QuikTranslationAdapter adapter = new QuikDefaultTranslator();

    private QuikTranslator() {
        // Nothing to see here.
    }
    
    public static void setAdapter(QuikTranslationAdapter adapter) {
        QuikTranslator.adapter = Objects.requireNonNull(adapter, "The translation adapter may not be null!");
    }
	
	public static String translateKeyOrDefault(String key, String def) {
		final String trans = adapter.translateKey(key);
		return trans.equals(key) ? def : trans;
	}

	public static String translate(Object key) {
		return adapter.translateKey(String.valueOf(key));
	}
	
	public static String translateIf(Object message, boolean condition) {
		return condition ? translate(message) : String.valueOf(message);
	}

	public static List<String> translate(List message) {
		if (message != null) {
			List translation = new ArrayList<>(message.size());
			message.forEach((e) -> translation.add(translate(e)));
			return translation;
		} else {
			return null;
		}
	}
	
	public static List<String> translateIf(List message, boolean condition) {
		return condition ? translate(message) : message;
	}
	
	public static String getLocale() {
		return adapter.getLocale();
	}

}
