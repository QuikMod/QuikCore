/*
 */
package com.github.quikmod.quikcore.log;

import com.github.quikmod.quikcore.config.QuikConfig;
import com.github.quikmod.quikcore.config.QuikConfigurableInstance;
import com.github.quikmod.quikcore.config.QuikConfigurable;
import com.github.quikmod.quikcore.defaults.QuikDefaultLog;
import com.github.quikmod.quikcore.injection.QuikInjector;
import com.github.quikmod.quikcore.lang.QuikTranslator;
import com.github.quikmod.quikcore.reflection.Quik;
import com.github.quikmod.quikcore.reflection.QuikDomain;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author RlonRyan
 */
@Quik
public class QuikLogger implements QuikConfigurableInstance {
    
    private static final ConcurrentHashMap<String, QuikLogger> LOGGERS = new ConcurrentHashMap<>();

    private static QuikLogAdapter adapter = new QuikDefaultLog();

    @QuikConfigurable(key = "${log} Logging", category = "Logging", comment = "Set to true to enable logging on the ${log} channel.")
    private boolean enabled = true;

    private final Object source;

    private QuikLogger(Object source) {
        this.source = source;
    }
    
    public static void setAdapter(QuikLogAdapter adapter) {
        QuikLogger.adapter = adapter;
    }

    @QuikInjector
    @QuikDomain("")
    public static QuikLogger getLogger(String source) {
        if (!LOGGERS.containsKey(source)) {
            QuikLogger logger = new QuikLogger(source);
            QuikConfig.addConfigurable(logger);
            LOGGERS.put(source, logger);
        }
        return LOGGERS.get(source);
    }
    
    

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void all(Object format, Object... objects) {
        if (this.enabled) {
            QuikLogger.adapter.all(source, QuikTranslator.translate(format), objects);
        }
    }

    public void severe(Object format, Object... objects) {
        if (this.enabled) {
            QuikLogger.adapter.severe(source, QuikTranslator.translate(format), objects);
        }
    }

    public void info(Object format, Object... objects) {
        if (this.enabled) {
            QuikLogger.adapter.info(source, QuikTranslator.translate(format), objects);
        }
    }

    public void warn(Object format, Object... objects) {
        if (this.enabled) {
            QuikLogger.adapter.warn(source, QuikTranslator.translate(format), objects);
        }
    }

    public void debug(Object format, Object... objects) {
        if (this.enabled) {
            QuikLogger.adapter.debug(source, QuikTranslator.translate(format), objects);
        }
    }

    public void error(Object format, Object... objects) {
        if (this.enabled) {
            QuikLogger.adapter.error(source, QuikTranslator.translate(format), objects);
        }
    }

    public void trace(Exception e) {
        if (this.enabled) {
            QuikLogger.adapter.trace(source, e);
        }
    }

    @Override
    public String resolve(String input) {
        return input.replaceAll("\\$\\{log\\}", String.valueOf(source));
    }

}
