/*
 */
package com.github.quikmod.quikcore.config;

import java.util.Set;

/**
 *
 * @author RlonRyan
 */
public interface QuikConfigAdapter {
	
	void save();
	
	public Set<String> getConfigs();
	
	public Set<String> getConfigCategories(String config);
	
	boolean getBoolean(String config, String category, String element, boolean defaultValue, String comment);
	
	int getInt(String config, String category, String element, int defaultValue, int minValue, int maxValue, String comment);
	
	float getFloat(String config, String category, String element, float defaultValue, float minValue, float maxValue, String comment);
	
	double getDouble(String config, String category, String element, double defaultValue, double minValue, double maxValue, String comment);
	
	String getString(String config, String category, String element, String defaultValue, String comment);
	
}
