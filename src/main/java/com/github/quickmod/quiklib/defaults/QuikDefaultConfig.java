/*
 */
package com.github.quickmod.quiklib.defaults;

import com.github.quickmod.quiklib.core.QuikCore;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import com.github.quickmod.quiklib.config.QuikConfigAdapter;

/**
 *
 * @author RlonRyan
 */
public class QuikDefaultConfig implements QuikConfigAdapter {

	private final Properties properties;
	private final Path path;

	public QuikDefaultConfig(Path path) {
		this.properties = new Properties();
		this.path = path;
	}

	@Override
	public void load() {
		QuikCore.getCoreLogger().debug("Loading Properties: " + this.path + "!");
		try (BufferedReader in = Files.newBufferedReader(this.path)) {
			this.properties.load(in);
			QuikCore.getCoreLogger().debug("Properties Loaded!");
		} catch (IOException e) {
			QuikCore.getCoreLogger().warn("Unable to load config!");
			QuikCore.getCoreLogger().trace(e);
		}
	}

	@Override
	public void save() {

		// Ensure path is good.
		this.path.getParent().toFile().mkdirs();
		
		QuikCore.getCoreLogger().debug("Saving Properties: " + this.path + "!");

		try (BufferedWriter out = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			this.properties.store(out, "AgriCore Configuration File");
			QuikCore.getCoreLogger().debug("Properties saved!");
		} catch (IOException e) {
			QuikCore.getCoreLogger().warn("Unable to save config!");
			QuikCore.getCoreLogger().trace(e);
		}

	}

	@Override
	public boolean getBoolean(String name, String category, boolean defaultValue, String comment) {
		if (properties.get(name) != null) {
			return Boolean.parseBoolean(this.properties.getProperty(name));
		} else {
			properties.setProperty(name, String.valueOf(defaultValue));
		}
		return defaultValue;
	}

	@Override
	public int getInt(String name, String category, int defaultValue, int minValue, int maxValue, String comment) {
		if (properties.get(name) != null) {
			try {
				return Integer.parseInt(this.properties.getProperty(name));
			} catch (NumberFormatException e) {
				QuikCore.getCoreLogger().warn("Bad configuration option for: " + category + ":" + name + "!");
			}
		} else {
			properties.setProperty(name, String.valueOf(defaultValue));
		}
		return defaultValue;
	}

	@Override
	public float getFloat(String name, String category, float defaultValue, float minValue, float maxValue, String comment) {
		if (properties.get(name) != null) {
			try {
				return Float.parseFloat(this.properties.getProperty(name));
			} catch (NumberFormatException e) {
				QuikCore.getCoreLogger().warn("Bad Float for Config: " + category + ":" + name + "!");
			}
		} else {
			properties.setProperty(name, String.valueOf(defaultValue));
		}
		return defaultValue;
	}
	
	@Override
	public double getDouble(String name, String category, double defaultValue, double minValue, double maxValue, String comment) {
		if (properties.get(name) != null) {
			try {
				return Double.parseDouble(this.properties.getProperty(name));
			} catch (NumberFormatException e) {
				QuikCore.getCoreLogger().warn("Bad Double for Config: " + category + ":" + name + "!");
			}
		} else {
			properties.setProperty(name, String.valueOf(defaultValue));
		}
		return defaultValue;
	}

	@Override
	public String getString(String name, String category, String defaultValue, String comment) {
		if (properties.get(name) != null) {
			return properties.getProperty(name);
		} else {
			properties.setProperty(name, defaultValue);
		}
		return defaultValue;
	}

	@Override
	public String getLocation() {
		return this.path.toString();
	}

	@Override
	public String toString() {
		return this.path.toString() + ": " + this.properties.toString();
	}

}
