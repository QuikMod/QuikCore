/*
 */
package com.github.quickmod.quiklib.defaults;

import com.github.quickmod.quiklib.util.QuikConverter;

/**
 *
 * @author RlonRyan
 */
public class QuikDefaultConverter implements QuikConverter {

	@Override
	public Object toStack(String element, int meta, int amount, String tags, boolean ignoreMeta, boolean ignoreTags, boolean useOreDict) {
		return String.format("Stack: { Element: '%s', Meta: %d, Amount: %d, Tags: '%s' }", element, meta, amount, tags);
	}

}
