/*
 */
package com.github.quikmod.quikcore.core;

import com.github.quikmod.quikcore.command.QuikCommandManager;
import com.github.quikmod.quikcore.config.QuikConfig;
import com.github.quikmod.quikcore.defaults.QuikDefaultConfig;
import com.github.quikmod.quikcore.defaults.QuikDefaultLog;
import com.github.quikmod.quikcore.defaults.QuikDefaultTranslator;
import com.github.quikmod.quikcore.lang.QuikTranslator;
import com.github.quikmod.quikcore.log.QuikLogManager;
import com.github.quikmod.quikcore.log.QuikLogger;
import java.nio.file.Paths;
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

	private static QuikLogManager logManager;

	private static QuikTranslator translator;

	private static QuikConverterManager converters;

	private static QuikCommandManager commands;

	private static QuikConfig config;

	private static QuikReflector reflector;

	private QuikCore() {
	}

	static {
		QuikCore.init(
				new QuikDefaultLog(),
				new QuikDefaultTranslator(),
				new QuikConverterManager(),
				new QuikCommandManager(),
				new QuikDefaultConfig(Paths.get("config")),
				new QuikReflector()
		);
	}

	public static void init(
			QuikLogAdapter log,
			QuikTranslationAdapter trans,
			QuikConverterManager converters,
			QuikCommandManager commands,
			QuikConfigAdapter provider,
			QuikReflector reflector
	) {
		QuikCore.logManager = new QuikLogManager(log);
		QuikCore.translator = new QuikTranslator(trans);
		QuikCore.config = new QuikConfig(provider);
		QuikLogger logger = QuikCore.getCoreLogger();
		logger.info("Initializing core!");
		QuikCore.converters = converters;
		QuikCore.commands = commands;
		QuikCore.reflector = reflector;
		logger.info("Initialized core!");
		logger.info("Saving config!");
		QuikCore.config.save();
		logger.info("Saved config!");
		QuikCore.reflector.registerLoader(converters::addConverters);
		QuikCore.reflector.registerLoader(config::addConfigurable);
		QuikCore.reflector.registerLoader(commands::addCommands);
		QuikCore.reflector.performLoad();
	}

	public static QuikLogger getCoreLogger() {
		return getLogger("QuikCore");
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
