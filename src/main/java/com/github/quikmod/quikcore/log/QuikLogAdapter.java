/*
 */
package com.github.quikmod.quikcore.log;

/**
 *
 * @author RlonRyan
 */
public interface QuikLogAdapter {

	void all(Object Source, String format, Object... object);

	void severe(Object Source, String format, Object... object);

	void info(Object Source, String format, Object... object);

	void warn(Object Source, String format, Object... object);

	void debug(Object Source, String format, Object... object);
	
	void error(Object Source, String format, Object... object);

	void trace(Object Source, Exception e);

}