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
import com.github.quikmod.quikcore.injection.QuikInjector;
import com.github.quikmod.quikcore.injection.QuikInjectorManager;
import com.github.quikmod.quikcore.lang.QuikTranslationAdapter;
import com.github.quikmod.quikcore.log.QuikLogAdapter;
import com.github.quikmod.quikcore.module.QuikModuleManager;
import com.github.quikmod.quikcore.network.QuikNetwork;
import com.github.quikmod.quikcore.network.QuikNetworkAdaptor;
import com.github.quikmod.quikcore.reflection.Quik;
import com.github.quikmod.quikcore.reflection.QuikDomain;
import com.github.quikmod.quikcore.reflection.QuikDomains;
import com.github.quikmod.quikcore.reflection.QuikReflector;
import com.github.quikmod.quikcore.register.QuikClassRegister;

/**
 *
 * @author RlonRyan
 */
@Quik
public final class QuikCore {

	private static QuikConfig config;

	private static QuikLogManager logger;

	private static QuikTranslator translator;

	private static QuikNetwork network;

	private static final QuikConverterManager converters = new QuikConverterManager();

	private static final QuikCommandManager commands = new QuikCommandManager();

	private static final QuikReflector reflector = new QuikReflector();
    
    private static final QuikModuleManager MODULE_MANAGER = new QuikModuleManager();

	private QuikCore() {
	}

	public static void init(
			QuikLogAdapter logAdaptor,
			QuikTranslationAdapter translatorAdaptor,
			QuikConfigAdapter configAdaptor,
			QuikNetworkAdaptor netAdaptor
	) {
		long start = System.currentTimeMillis();
		QuikCore.logger = new QuikLogManager(logAdaptor);
		QuikCore.translator = new QuikTranslator(translatorAdaptor);
		QuikCore.config = new QuikConfig(configAdaptor);
		QuikLogger log = QuikCore.getCoreLogger();
		QuikCore.network = new QuikNetwork(netAdaptor);
		QuikCore.reflector.performLoad();
		long end = System.currentTimeMillis();
		log.info("QuikCore Initialized! ({0} ms)", end - start);
        
        log.info("Loading modules!");
		start = System.currentTimeMillis();
		MODULE_MANAGER.findModules(reflector);
        MODULE_MANAGER.createModules();
        MODULE_MANAGER.sortModules();
        MODULE_MANAGER.scanModules(reflector);
        MODULE_MANAGER.initModules();
        MODULE_MANAGER.loadModules(reflector);
		end = System.currentTimeMillis();
		log.info("Loaded Modules! ({0} ms)", end - start);

		log.info("Performing injections!");
		start = System.currentTimeMillis();
		MODULE_MANAGER.getModule(QuikInjectorManager.class).ifPresent(m -> m.getInstance().performInjections());
		end = System.currentTimeMillis();
		log.info("Performed Injections! ({0} ms)", end - start);

		log.info("Loading config!");
		start = System.currentTimeMillis();
		QuikCore.config.load();
		end = System.currentTimeMillis();
		log.info("Loaded config! ({0} ms)", end - start);

		log.info("Saving config!");
		start = System.currentTimeMillis();
		QuikCore.config.save();
		end = System.currentTimeMillis();
		log.info("Saved config! ({0} ms)", end - start);
	}

	@QuikClassRegister("main_register")
	private static void registerClass(Class<?> clazz) {
		QuikCore.converters.addConverters(clazz);
		QuikCore.config.addConfigurable(clazz);
		QuikCore.commands.addCommands(clazz);
		QuikCore.network.registerMessage(clazz);
	}

	public static QuikLogger getCoreLogger() {
		return getLogger("QuikCore");
	}

	@QuikInjector
	@QuikDomain("")
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

	public static QuikDomains getDomains() {
		return reflector.getDomains();
	}
    
    public static QuikModuleManager getModuleManger() {
        return MODULE_MANAGER;
    }

}
