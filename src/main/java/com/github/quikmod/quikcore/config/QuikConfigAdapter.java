/*
 */
package com.github.quikmod.quikcore.config;

/**
 *
 * @author RlonRyan
 */
public interface QuikConfigAdapter {
	
	void save();
	
	boolean getBoolean(String config, String name, String category, boolean defaultValue, String comment);
	
	int getInt(String config, String name, String category, int defaultValue, int minValue, int maxValue, String comment);
	
	float getFloat(String config, String name, String category, float defaultValue, float minValue, float maxValue, String comment);
	
	double getDouble(String config, String name, String category, double defaultValue, double minValue, double maxValue, String comment);
	
	String getString(String config, String name, String category, String defaultValue, String comment);
	
}
