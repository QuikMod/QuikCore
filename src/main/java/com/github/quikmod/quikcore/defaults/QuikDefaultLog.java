/*
 */
package com.github.quikmod.quikcore.defaults;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.quikmod.quikcore.log.QuikLogAdapter;

/**
 *
 * @author RlonRyan
 */
public class QuikDefaultLog implements QuikLogAdapter {

	public static final Logger logger = Logger.getLogger("QuikCore");

	public void log(Level level, String format, Object... objects) {
		logger.log((Level) level, format, objects);
	}

	@Override
	public void all(Object source, String format, Object... objects) {
		log(Level.ALL, "[" + source + "]: " + format, objects);
	}

	@Override
	public void severe(Object source, String format, Object... objects) {
		log(Level.SEVERE, "[" + source + "]: " + format, objects);
	}

	@Override
	public void info(Object source, String format, Object... objects) {
		log(Level.INFO, "[" + source + "]: " + format, objects);
	}

	@Override
	public void warn(Object source, String format, Object... objects) {
		log(Level.WARNING, "[" + source + "]: " + format, objects);
	}

	@Override
	public void error(Object source, String format, Object... objects) {
		log(Level.SEVERE, "[" + source + "]: " + format, objects);
	}

	@Override
	public void debug(Object source, String format, Object... objects) {
		log(Level.INFO, "[" + source + "]: [DEBUG]: " + format, objects);
	}

	@Override
	public void trace(Object source, Exception e) {
		e.printStackTrace();
	}

}
