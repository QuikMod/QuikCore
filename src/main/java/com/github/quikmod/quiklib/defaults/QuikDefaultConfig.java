/*
 */
package com.github.quikmod.quiklib.defaults;

import com.github.quikmod.quiklib.core.QuikCore;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import com.github.quikmod.quiklib.config.QuikConfigAdapter;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RlonRyan
 */
public class QuikDefaultConfig implements QuikConfigAdapter {

	private final Map<String, Properties> properties;
	private final Path configDir;

	public QuikDefaultConfig(Path path) {
		this.properties = new HashMap<>();
		this.configDir = path;
	}

	@Override
	public void save() {
		properties.forEach(this::save);
	}
	
	public void save(String path, Properties properties) {

		// Ensure path is good.
		this.configDir.toFile().mkdirs();
		
		final Path toSave = this.configDir.resolve(path);
		
		QuikCore.getCoreLogger().debug("Saving Properties: " + toSave + "!");

		try (BufferedWriter out = Files.newBufferedWriter(toSave, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			properties.store(out, "QuikCore Configuration File");
			QuikCore.getCoreLogger().debug("Properties saved!");
		} catch (IOException e) {
			QuikCore.getCoreLogger().warn("Unable to save config!");
			QuikCore.getCoreLogger().trace(e);
		}

	}
	
	public void load(String config, Properties properties) {
		final Path path = this.configDir.resolve(config);
		try (BufferedReader in = Files.newBufferedReader(path)) {
			properties.load(in);
		} catch (IOException e) {
			// Since logger won't work here...
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	private Properties getOrCreate(String config) {
		Properties props = this.properties.get(config);
		if (props == null) {
			props = new Properties();
			this.load(config, props);
			this.properties.put(config, props);
		}
		return props;
	}

	@Override
	public boolean getBoolean(String config, String name, String category, boolean defaultValue, String comment) {
		final Properties props = this.getOrCreate(config);
		if (props.get(name) != null) {
			return Boolean.parseBoolean(props.getProperty(name));
		} else {
			props.setProperty(name, String.valueOf(defaultValue));
		}
		return defaultValue;
	}

	@Override
	public int getInt(String config, String name, String category, int defaultValue, int minValue, int maxValue, String comment) {
		final Properties props = this.getOrCreate(config);
		if (props.get(name) != null) {
			try {
				return Integer.parseInt(props.getProperty(name));
			} catch (NumberFormatException e) {
				QuikCore.getCoreLogger().warn("Bad configuration option for: " + category + ":" + name + "!");
			}
		} else {
			props.setProperty(name, String.valueOf(defaultValue));
		}
		return defaultValue;
	}

	@Override
	public float getFloat(String config, String name, String category, float defaultValue, float minValue, float maxValue, String comment) {
		final Properties props = this.getOrCreate(config);
		if (props.get(name) != null) {
			try {
				return Float.parseFloat(props.getProperty(name));
			} catch (NumberFormatException e) {
				QuikCore.getCoreLogger().warn("Bad Float for Config: " + category + ":" + name + "!");
			}
		} else {
			props.setProperty(name, String.valueOf(defaultValue));
		}
		return defaultValue;
	}
	
	@Override
	public double getDouble(String config, String name, String category, double defaultValue, double minValue, double maxValue, String comment) {
		final Properties props = this.getOrCreate(config);
		if (props.get(name) != null) {
			try {
				return Double.parseDouble(props.getProperty(name));
			} catch (NumberFormatException e) {
				QuikCore.getCoreLogger().warn("Bad Double for Config: " + category + ":" + name + "!");
			}
		} else {
			props.setProperty(name, String.valueOf(defaultValue));
		}
		return defaultValue;
	}

	@Override
	public String getString(String config, String name, String category, String defaultValue, String comment) {
		final Properties props = this.getOrCreate(config);
		if (props.get(name) != null) {
			return props.getProperty(name);
		} else {
			props.setProperty(name, defaultValue);
		}
		return defaultValue;
	}

	@Override
	public String toString() {
		return this.configDir.toString() + ": " + this.properties.toString();
	}

}
