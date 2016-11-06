/*
 */
package com.github.quickmod.quiklib.log;

import com.github.quickmod.quiklib.config.QuikConfigCategory;
import com.github.quickmod.quiklib.core.QuikCore;
import com.github.quickmod.quiklib.config.QuikConfigurableInstance;
import com.github.quickmod.quiklib.config.QuikConfigurable;

/**
 *
 * @author RlonRyan
 */
public class QuikLogger implements QuikConfigurableInstance {
	
	private final QuikLogAdapter adapter;
	private final Object source;
	
	@QuikConfigurable(key = "${log} Logging", category = QuikConfigCategory.LOGGING, comment = "Set to true to enable logging on the ${log} channel.")
	private boolean enabled = true;

	QuikLogger(QuikLogAdapter adapter, Object source) {
		this.adapter = adapter;
		this.source = source;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void all(Object format, Object... objects) {
		if (this.enabled) {
			adapter.all(source, QuikCore.getTranslator().translate(format), objects);
		}
	}

	public void severe(Object format, Object... objects) {
		if (this.enabled) {
			adapter.severe(source, QuikCore.getTranslator().translate(format), objects);
		}
	}

	public void info(Object format, Object... objects) {
		if (this.enabled) {
			adapter.info(source, QuikCore.getTranslator().translate(format), objects);
		}
	}

	public void warn(Object format, Object... objects) {
		if (this.enabled) {
			adapter.warn(source, QuikCore.getTranslator().translate(format), objects);
		}
	}
	
	public void debug(Object format, Object... objects) {
		if (this.enabled) {
			adapter.debug(source, QuikCore.getTranslator().translate(format), objects);
		}
	}

	public void error(Object format, Object... objects) {
		if (this.enabled) {
			adapter.error(source, QuikCore.getTranslator().translate(format), objects);
		}
	}

	public void trace(Exception e) {
		if (this.enabled) {
			adapter.trace(source, e);
		}
	}

	@Override
	public String resolve(String input) {
		return input.replaceAll("\\$\\{log\\}", String.valueOf(source));
	}

}
