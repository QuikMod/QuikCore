/*
 */
package com.github.quikmod.quikcore.core;

import com.github.quikmod.quikcore.command.QuikCommandManager;
import com.github.quikmod.quikcore.config.QuikConfig;
import com.github.quikmod.quikcore.lang.QuikTranslator;
import com.github.quikmod.quikcore.log.QuikLogManager;
import com.github.quikmod.quikcore.log.QuikLogger;
import com.github.quikmod.quikcore.config.QuikConfigAdapter;
import com.github.quikmod.quikcore.conversion.QuikConverterManager;
import com.github.quikmod.quikcore.lang.QuikTranslationAdapter;
import com.github.quikmod.quikcore.log.QuikLogAdapter;
import com.github.quikmod.quikcore.reflection.QuikReflector;

/**
 *
 * @author RlonRyan
 */
public final class QuikCore {

	private static QuikConfig config;
	
	private static QuikLogManager logger;

	private static QuikTranslator translator;
	
	private static final QuikConverterManager converters = new QuikConverterManager();

	private static final QuikCommandManager commands = new QuikCommandManager();

	private static final QuikReflector reflector = new QuikReflector();

	private QuikCore() {
	}

	public static void init(
			QuikLogAdapter logAdaptor,
			QuikTranslationAdapter translatorAdaptor,
			QuikConfigAdapter configAdaptor
	) {
		QuikCore.logger = new QuikLogManager(logAdaptor);
		QuikCore.translator = new QuikTranslator(translatorAdaptor);
		QuikCore.config = new QuikConfig(configAdaptor);
		QuikLogger log = QuikCore.getCoreLogger();
		log.info("QuikCore Initialized!");
		log.info("Registering Default Loaders!");
		QuikCore.reflector.registerLoader(converters::addConverters);
		QuikCore.reflector.registerLoader(config::addConfigurable);
		QuikCore.reflector.registerLoader(commands::addCommands);
		log.info("Registered Default Loaders!");
		QuikCore.reflector.performLoad();
		log.info("Loading config!");
		QuikCore.config.load();
		log.info("Loaded config!");
		log.info("Saving config!");
		QuikCore.config.save();
		log.info("Saved config!");
	}

	public static QuikLogger getCoreLogger() {
		return getLogger("QuikCore");
	}

	public static QuikLogger getLogger(String source) {
		return logger.getLogger(source);
	}

	public static QuikTranslator getTranslator() {
		return translator;
	}

	public static QuikLogManager getLogManager() {
		return logger;
	}

	public static QuikConverterManager getConverters() {
		return converters;
	}

	public static QuikCommandManager getCommands() {
		return commands;
	}

	public static QuikConfig getConfig() {
		return config;
	}

}
