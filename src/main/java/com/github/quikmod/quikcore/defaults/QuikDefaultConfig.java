/*
 */
package com.github.quikmod.quikcore.defaults;

import com.github.quikmod.quikcore.core.QuikCore;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import com.github.quikmod.quikcore.config.QuikConfigAdapter;
import com.github.quikmod.quikcore.util.TypeHelper;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public Set<String> getConfigs() {
		return TypeHelper.asSet("Default");
	}

	@Override
	public Set<String> getConfigCategories(String config) {
		return TypeHelper.asSet("Default");
	}

	@Override
	public boolean getBoolean(String config, String category, String element, boolean defaultValue, String comment) {
		final Properties props = this.getOrCreate(config);
		if (props.get(element) != null) {
			return Boolean.parseBoolean(props.getProperty(element));
		} else {
			props.setProperty(element, String.valueOf(defaultValue));
		}
		return defaultValue;
	}

	@Override
	public int getInt(String config, String category, String element, int defaultValue, int minValue, int maxValue, String comment) {
		final Properties props = this.getOrCreate(config);
		if (props.get(element) != null) {
			try {
				return Integer.parseInt(props.getProperty(element));
			} catch (NumberFormatException e) {
				QuikCore.getCoreLogger().warn("Bad configuration option for: " + category + ":" + element + "!");
			}
		} else {
			props.setProperty(element, String.valueOf(defaultValue));
		}
		return defaultValue;
	}

	@Override
	public float getFloat(String config, String category, String element, float defaultValue, float minValue, float maxValue, String comment) {
		final Properties props = this.getOrCreate(config);
		if (props.get(element) != null) {
			try {
				return Float.parseFloat(props.getProperty(element));
			} catch (NumberFormatException e) {
				QuikCore.getCoreLogger().warn("Bad Float for Config: " + category + ":" + element + "!");
			}
		} else {
			props.setProperty(element, String.valueOf(defaultValue));
		}
		return defaultValue;
	}
	
	@Override
	public double getDouble(String config, String category, String element, double defaultValue, double minValue, double maxValue, String comment) {
		final Properties props = this.getOrCreate(config);
		if (props.get(element) != null) {
			try {
				return Double.parseDouble(props.getProperty(element));
			} catch (NumberFormatException e) {
				QuikCore.getCoreLogger().warn("Bad Double for Config: " + category + ":" + element + "!");
			}
		} else {
			props.setProperty(element, String.valueOf(defaultValue));
		}
		return defaultValue;
	}

	@Override
	public String getString(String config, String category, String element, String defaultValue, String comment) {
		final Properties props = this.getOrCreate(config);
		if (props.get(element) != null) {
			return props.getProperty(element);
		} else {
			props.setProperty(element, defaultValue);
		}
		return defaultValue;
	}

	@Override
	public String toString() {
		return this.configDir.toString() + ": " + this.properties.toString();
	}

}
