/*
 */
package com.github.quikmod.quikcore.log;

import com.github.quikmod.quikcore.core.QuikCore;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RlonRyan
 */
public class QuikLogManager {
	
	private final QuikLogAdapter adapter;
	
	private final Map<String, QuikLogger> loggers;

	public QuikLogManager(QuikLogAdapter adapter) {
		this.adapter = adapter;
		this.loggers = new HashMap<>();
	}
	
	public QuikLogger getLogger(String source) {
		if (!loggers.containsKey(source)) {
			QuikLogger logger = new QuikLogger(adapter, source);
			QuikCore.getConfig().addConfigurable(logger);
			loggers.put(source, logger);
		}
		return loggers.get(source);
	}
	
}
