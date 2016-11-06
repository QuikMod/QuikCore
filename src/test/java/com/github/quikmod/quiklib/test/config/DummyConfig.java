/*
 */
package com.github.quikmod.quiklib.test.config;

import com.github.quickmod.quiklib.config.QuikConfigCategory;
import com.github.quickmod.quiklib.core.QuikCore;
import java.lang.reflect.Field;
import com.github.quickmod.quiklib.config.QuikConfigurable;

/**
 *
 * @author RlonRyan
 */
public class DummyConfig {
	
	@QuikConfigurable(
			category = QuikConfigCategory.CORE,
			comment = "Is it true?",
			key = "is_true"
	)
	public static boolean isTrue = false;
	
	@QuikConfigurable(
			category = QuikConfigCategory.CORE,
			comment = "Give me an integer!",
			key = "the_int"
	)
	public static int theInt = 10;
	
	@QuikConfigurable(
			category = QuikConfigCategory.CORE,
			comment = "Float my boat!",
			key = "the_float"
	)
	public static float theFloat = -1.25f;
	
	@QuikConfigurable(
			category = QuikConfigCategory.CORE,
			comment = "1 <3 π!",
			key = "the_double"
	)
	public static double theDouble = Math.PI;
	
	@QuikConfigurable(
			category = QuikConfigCategory.CORE,
			comment = "Random String.",
			key = "the_string"
	)
	public static String theString = "The quick brown fox jumps over the lazy dog.";
	
	public static String asString() {
		final StringBuilder sb = new StringBuilder().append("Dummy Config:\n");
		for (Field f : DummyConfig.class.getFields()) {
			try {
				sb.append("\t- ").append(f.getName()).append(": ").append(f.get(null)).append("\n");
			} catch (IllegalAccessException | IllegalArgumentException e) {
				QuikCore.getCoreLogger().trace(e);
			}
		}
		return sb.toString();
	}
	
}
