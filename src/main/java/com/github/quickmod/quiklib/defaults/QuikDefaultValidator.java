/*
 */
package com.github.quickmod.quiklib.defaults;

import com.github.quickmod.quiklib.core.QuikCore;
import com.github.quickmod.quiklib.util.QuikValidator;

/**
 *
 * @author RlonRyan
 */
public class QuikDefaultValidator implements QuikValidator {

	@Override
	public boolean isValidBlock(String block) {
		if (block == null) {
			QuikCore.getCoreLogger().warn("Null Block!");
			return false;
		}
		QuikCore.getCoreLogger().warn("Faking valid result for block: " + block + "!");
		return true;
	}

	@Override
	public boolean isValidItem(String item) {
		if (item == null) {
			QuikCore.getCoreLogger().warn("Null Item!");
			return false;
		}
		QuikCore.getCoreLogger().warn("Faking valid result for item: " + item + "!");
		return true;
	}

	@Override
	public boolean isValidTexture(String texture) {
		if (texture == null) {
			QuikCore.getCoreLogger().warn("Null Texture!");
			return false;
		}
		QuikCore.getCoreLogger().warn("Faking valid result for texture: " + texture + "!");
		return true;
	}

}
