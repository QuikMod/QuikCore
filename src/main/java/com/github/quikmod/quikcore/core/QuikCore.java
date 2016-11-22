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
import com.github.quikmod.quikcore.reflection.Quik;
import com.github.quikmod.quikcore.reflection.QuikReflector;
import com.github.quikmod.quikcore.reflection.QuikRegister;

/**
 *
 * @author RlonRyan
 */
@Quik
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
		QuikCore.reflector.performLoad();
		long start = System.currentTimeMillis();
		log.info("Loading config!");
		QuikCore.config.load();
		long end = System.currentTimeMillis();
		log.info("Loaded config! ({0} ms)", end - start);
		log.info("Saving config!");
		start = System.currentTimeMillis();
		QuikCore.config.save();
		end = System.currentTimeMillis();
		log.info("Saved config! ({0} ms)", end - start);
	}
	
	@QuikRegister
	private static void registerClass(Class<?> clazz) {
		QuikCore.converters.addConverters(clazz);
		QuikCore.config.addConfigurable(clazz);
		QuikCore.commands.addCommands(clazz);
		QuikCore.getCoreLogger().info("Registered Core Registers!");
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
