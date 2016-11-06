/*
 */
package com.github.quickmod.quiklib.core;

import com.github.quickmod.quiklib.config.QuikConfig;
import com.github.quickmod.quiklib.defaults.QuikDefaultConfig;
import com.github.quickmod.quiklib.defaults.QuikDefaultConverter;
import com.github.quickmod.quiklib.defaults.QuikDefaultLog;
import com.github.quickmod.quiklib.defaults.QuikDefaultTranslator;
import com.github.quickmod.quiklib.defaults.QuikDefaultValidator;
import com.github.quickmod.quiklib.lang.QuikTranslator;
import com.github.quickmod.quiklib.log.QuikLogManager;
import com.github.quickmod.quiklib.log.QuikLogger;
import java.nio.file.Paths;
import com.github.quickmod.quiklib.config.QuikConfigAdapter;
import com.github.quickmod.quiklib.lang.QuikTranslationAdapter;
import com.github.quickmod.quiklib.log.QuikLogAdapter;
import com.github.quickmod.quiklib.util.QuikConverter;
import com.github.quickmod.quiklib.util.QuikValidator;

/**
 *
 * @author RlonRyan
 */
public final class QuikCore {
	
	private static QuikLogManager logManager;
	
	private static QuikTranslator translator;
	
	private static QuikValidator validator;
	
	private static QuikConverter converter;
	
	private static QuikConfig config;
	
	private QuikCore() {
	}
	
	static {
		QuikCore.init(new QuikDefaultLog(),
				new QuikDefaultTranslator(),
				new QuikDefaultValidator(),
				new QuikDefaultConverter(),
				new QuikDefaultConfig(Paths.get("config", "AgriCore", "agricore.config"))
		);
	}
	
	public static void init(
			QuikLogAdapter log,
			QuikTranslationAdapter trans,
			QuikValidator validator,
			QuikConverter converter,
			QuikConfigAdapter provider
	) {
		QuikCore.logManager = new QuikLogManager(log);
		QuikCore.translator = new QuikTranslator(trans);
		QuikCore.config = new QuikConfig(provider);
		QuikLogger logger = QuikCore.getCoreLogger();
		logger.info("Initializing core!");
		QuikCore.validator = validator;
		QuikCore.converter = converter;
		logger.info("Loading config!");
		QuikCore.config.load();
		logger.info("Loaded config!");
		logger.info("Configuring modules!");
		QuikCore.config.addConfigurable(logger);
		QuikCore.config.addConfigurable(validator);
		logger.info("Configured modules!");
		logger.info("Saving config!");
		QuikCore.config.save();
		logger.info("Saved config!");
		logger.info("Initialized core!");
	}
	
	public static QuikLogger getCoreLogger() {
		return getLogger("AgriCore");
	}
	
	public static QuikLogger getLogger(Object source) {
		return logManager.getLogger(source);
	}
	
	public static QuikTranslator getTranslator() {
		return translator;
	}
	
	public static QuikLogManager getLogManager() {
		return logManager;
	}
	
	public static QuikValidator getValidator() {
		return validator;
	}
	
	public static QuikConverter getConverter() {
		return converter;
	}
	
	public static QuikConfig getConfig() {
		return config;
	}
	
}
