/*
 */
package com.github.quikmod.quikcore.core;

import com.github.quikmod.quikcore.command.QuikCommand;
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
import com.github.quikmod.quikcore.config.QuikConfigurable;
import com.github.quikmod.quikcore.conversion.QuikConverter;
import com.github.quikmod.quikcore.conversion.QuikConverterManager;
import com.github.quikmod.quikcore.lang.QuikTranslationAdapter;
import com.github.quikmod.quikcore.log.QuikLogAdapter;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

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
	
	private QuikCore() {
	}
	
	static {
		QuikCore.init(new QuikDefaultLog(),
				new QuikDefaultTranslator(),
				new QuikConverterManager(),
				new QuikCommandManager(),
				new QuikDefaultConfig(Paths.get("config"))
		);
	}
	
	public static void init(
			QuikLogAdapter log,
			QuikTranslationAdapter trans,
			QuikConverterManager converters,
			QuikCommandManager commands,
			QuikConfigAdapter provider
	) {
		QuikCore.logManager = new QuikLogManager(log);
		QuikCore.translator = new QuikTranslator(trans);
		QuikCore.config = new QuikConfig(provider);
		QuikLogger logger = QuikCore.getCoreLogger();
		logger.info("Initializing core!");
		QuikCore.converters = converters;
		QuikCore.commands = commands;
		logger.info("Saving config!");
		QuikCore.config.save();
		logger.info("Saved config!");
		logger.info("Initialized core!");
		load();
	}
	
	private static void load() {
		Reflections mirror = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage("com.github.quikmod.quikcore"))
				.setScanners(new FieldAnnotationsScanner(), new MethodAnnotationsScanner())
		);
		mirror.getFieldsAnnotatedWith(QuikConfigurable.class).forEach(config::addConfigurable);
		mirror.getMethodsAnnotatedWith(QuikConverter.class).forEach(converters::addConverter);
		// Command addition must come after converter scanning.
		mirror.getMethodsAnnotatedWith(QuikCommand.class).forEach(commands::attemptAddCommand);
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
