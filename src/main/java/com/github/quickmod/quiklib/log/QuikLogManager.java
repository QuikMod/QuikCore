/*
 */
package com.github.quickmod.quiklib.log;

import com.github.quickmod.quiklib.core.QuikCore;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RlonRyan
 */
public class QuikLogManager {
	
	private final QuikLogAdapter adapter;
	
	private final Map<Object, QuikLogger> loggers;

	public QuikLogManager(QuikLogAdapter adapter) {
		this.adapter = adapter;
		this.loggers = new HashMap<>();
	}
	
	public QuikLogger getLogger(Object source) {
		if (!loggers.containsKey(source)) {
			QuikLogger logger = new QuikLogger(adapter, source);
			QuikCore.getConfig().addConfigurable(logger);
			loggers.put(source, logger);
		}
		return loggers.get(source);
	}
	
}
